package com.sub6resources.vampir.services

import android.app.Notification
import android.app.Notification.VISIBILITY_SECRET
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import com.sub6resources.utilities.sharedPreferences
import com.sub6resources.utilities.toApi
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
                Log.d("Vampïr", "      Asking le server for prediction")
                val predictionResponse = predictApi.predictBloodSugar(encryptedCredentials)
                predictionResponse.subscribe(
                        {
                            Log.d("Vampïr", "Success! ${it.predictions}")
                            val notificationConstant = NotificationCompat.Builder(applicationContext, "glucose_001").apply {
                                setSmallIcon(R.drawable.ic_blood_drop)
                                setContentText("Glucose Level - ${it.predictions[0]} mg/dL. In 20 mins - ${it.predictions[it.predictions.size - 1]} mg/dL")
                                setSubText("${it.predictions[0]} mg/dL")
                                setStyle(NotificationCompat.BigTextStyle().bigText("Currently ${it.predictions[0]} mg/dL.\n\nWe predict it will reach ${Math.floor(it.predictions[it.predictions.size - 1].toDouble())} mg/dL in ${(it.predictions.size - 1) * 5} minutes"))
                                setTicker("${it.predictions[0]} milligrams per deciliter")
                                toApi(Build.VERSION_CODES.O) {
                                    priority = Notification.PRIORITY_LOW
                                }
                                setOngoing(true)
                                setShowWhen(false)
                                setVisibility(VISIBILITY_SECRET)
                            }.build()

                            val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val channel = NotificationChannel("glucose_001",
                                        "Glucose Level Monitors",
                                        NotificationManager.IMPORTANCE_LOW)
                                channel.setShowBadge(false)
                                channel.description = "Shows a constant low priority notification with your last glucose level reading."
                                mNotificationManager.createNotificationChannel(channel)
                            }

                            mNotificationManager.notify(42, notificationConstant)

                            it.predictions.forEachIndexed { i, prediction ->
                                if(prediction < sharedPreferences.getInt("notifyLow", 50) || prediction > sharedPreferences.getInt("notifyHigh", 300)) {

                                    var index = i

                                    index = if(prediction < sharedPreferences.getInt("notifyLow", 50)) {
                                        it.predictions.indexOf(it.predictions.min())
                                    } else {
                                        it.predictions.indexOf(it.predictions.max())
                                    }
                                    val notification = NotificationCompat.Builder(applicationContext, "glucose_002")
                                            .setSmallIcon(R.drawable.ic_blood_drop)
                                            .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.drawable.vampir_logo))
                                            .setContentTitle("Glucose Level Alert")
                                            .setContentText("Glucose may be at ${Math.floor(it.predictions[index].toDouble())} mg/dL in ${index*5} minutes")
                                            .setPriority(Notification.PRIORITY_MAX)
                                            .build()

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        val channel = NotificationChannel("glucose_002",
                                                "Dangerous Glucose Level Alert",
                                                NotificationManager.IMPORTANCE_HIGH)
                                        channel.description = "Alerts you if your glucose levels are predicted to go above or below the safe range. The safe range can be edited in settings."
                                        mNotificationManager.createNotificationChannel(channel)
                                    }

                                    mNotificationManager.notify(43, notification)

                                    jobFinished(job, false)
                                    return@subscribe
                                }
                            }


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