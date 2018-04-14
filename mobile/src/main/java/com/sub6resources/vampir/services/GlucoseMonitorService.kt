package com.sub6resources.vampir.services

import android.app.Notification
import android.util.Log
import br.com.goncalves.pugnotification.notification.PugNotification
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import com.sub6resources.utilities.sharedPreferences
import com.sub6resources.utilities.startActivity
import com.sub6resources.vampir.MainActivity
import com.sub6resources.vampir.R
import com.sub6resources.vampir.api.PredictionApi
import com.sub6resources.vampir.loggedWithAuthToken
import com.sub6resources.vampir.models.EncryptedCredentials
import kotlinx.coroutines.experimental.async
import retrofit2.Retrofit
import java.util.*

class GlucoseMonitorService: JobService() {
    override fun onStartJob(job: JobParameters): Boolean {
        async {
            Log.d("Vampïr", "Glucose Monitoring...")
            val retrofit = Retrofit.Builder().loggedWithAuthToken("http://vampirai.ryanberger.me", "token")
            val predictApi = retrofit.create(PredictionApi::class.java)

            val encryptedCredentials = EncryptedCredentials(sharedPreferences.getString("encryptedRealtimeCredentials", ""), Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
            if(encryptedCredentials.encryptedCredentials.isNotEmpty()) {
                Log.d("Vampïr", "Asking le server for prediction")
                val predictionResponse = predictApi.predictBloodSugar(encryptedCredentials)
                predictionResponse.subscribe(
                        {
                            Log.d("Vampïr", "Success! ${it.predictions}")
                            PugNotification.with(applicationContext)
                                    .load()
                                    .title("Vampïr -- ${it.predictions[0]}")
                                    .tag("vampir")
                                    .message("Current blood glucose level")
                                    .largeIcon(R.mipmap.ic_launcher_foreground)
                                    .smallIcon(R.mipmap.ic_launcher_foreground)
                                    .ongoing(true)
                                    .flags(Notification.DEFAULT_ALL)
                                    .simple()
                                    .build()
                            jobFinished(job, false)
                        },
                        {
                            Log.d("Vampïr", "Failure :( ${it.message}")
                            jobFinished(job, false)
                        }
                )
            } else {
                Log.d("Vampïr", "Done with background job")
                jobFinished(job, false)
            }


        }
        return true
    }

    override fun onStopJob(job: JobParameters) = false
}