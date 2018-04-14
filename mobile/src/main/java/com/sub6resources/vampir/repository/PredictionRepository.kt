package com.sub6resources.vampir.repository

import com.sub6resources.vampir.BasicNetworkState
import com.sub6resources.vampir.api.PredictionApi
import com.sub6resources.vampir.makeNetworkRequest
import com.sub6resources.vampir.models.EncryptedCredentials
import com.sub6resources.vampir.models.PredictionResponse

class PredictionRepository(private val predictionApi: PredictionApi) {
    fun predictBloodSugar(encryptedCredentials: EncryptedCredentials) = makeNetworkRequest(predictionApi.predictBloodSugar(encryptedCredentials)) {
        onError {
            it.printStackTrace()
            BasicNetworkState.Error("Le New Error")
        }
    }
}