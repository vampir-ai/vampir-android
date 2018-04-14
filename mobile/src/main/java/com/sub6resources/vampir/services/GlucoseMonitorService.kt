package com.sub6resources.vampir.services

import android.arch.lifecycle.Observer
import android.util.Log
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import com.sub6resources.utilities.sharedPreferences
import com.sub6resources.vampir.BasicNetworkState
import com.sub6resources.vampir.api.PredictionApi
import com.sub6resources.vampir.loggedWithAuthToken
import com.sub6resources.vampir.makeNetworkRequest
import com.sub6resources.vampir.models.EncryptedCredentials
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.async
import retrofit2.Retrofit

class GlucoseMonitorService: JobService() {
    override fun onStartJob(job: JobParameters): Boolean {
        async {
            Log.d("Vampïr", "Glucose Monitoring...")
            val retrofit = Retrofit.Builder().loggedWithAuthToken("http://vampirai.ryanberger.me", "token")
            val predictApi = retrofit.create(PredictionApi::class.java)

            val encryptedCredentials = EncryptedCredentials(sharedPreferences.getString("encryptedRealtimeCredentials", ""))
            if(encryptedCredentials.encryptedCredentials.isNotEmpty()) {
                Log.d("Vampïr", "Asking le server for prediction")
                val predictionResponse = predictApi.predictBloodSugar(encryptedCredentials)
                predictionResponse.observeOn(Schedulers.newThread()).doOnSuccess {
                    Log.d("Vampïr", "Success! ${it.predictions}")
                }.doOnError {
                    Log.d("Vampïr", "Error :( ${it.message}")
                }
            }

        }
        return true
    }

    override fun onStopJob(job: JobParameters) = false
}