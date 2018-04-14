package com.sub6resources.vampir.services

import android.util.Log
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import com.sub6resources.utilities.sharedPreferences
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