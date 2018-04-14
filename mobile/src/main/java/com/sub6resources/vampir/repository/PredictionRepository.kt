package com.sub6resources.vampir.repository

import com.sub6resources.vampir.BasicNetworkState
import com.sub6resources.vampir.api.PredictionApi
import com.sub6resources.vampir.makeNetworkRequest
import com.sub6resources.vampir.models.EncryptedCredentials
import retrofit2.HttpException

class PredictionRepository(private val predictionApi: PredictionApi) {
    fun predictBloodSugar(encryptedCredentials: EncryptedCredentials) = makeNetworkRequest(predictionApi.predictBloodSugar(encryptedCredentials)) {
        onError {
            if(it is HttpException) {
                if(it.code() == 401 || it.code() == 500) {
                    BasicNetworkState.Error("Incorrect username or password")
                } else {
                    BasicNetworkState.Error("Unknown error: " + it.code())
                }
            } else {
                BasicNetworkState.Error("Unknown error")
            }
        }
    }
}