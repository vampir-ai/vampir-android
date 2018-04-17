package com.sub6resources.vampir.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
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
                            val notBuilder = startForeground()
                            val notification = notBuilder.apply {
                                setContentTitle("Vampïr -- ${it.predictions[0]} mg/dL")
                                setContentText("Vampïr Dexcom Realtime Data")
                                setSmallIcon(R.drawable.vampir_logo)
                            }.build()

                            val notificationManager = NotificationManagerCompat.from(applicationContext)
                            notificationManager.notify(5432, notification)


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

        private fun startForeground(): NotificationCompat.Builder {
            val channelId =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        createNotificationChannel()
                    } else {
                        // If earlier version channel ID is not used
                        // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                        ""
                    }

            val notificationBuilder = NotificationCompat.Builder(this, channelId )
            return notificationBuilder
        }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String{
        val channelId = "glucose_data"
        val channelName = "Glucose Notification"
        val chan = NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }
}