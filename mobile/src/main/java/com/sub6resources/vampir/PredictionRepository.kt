package com.sub6resources.vampir

import com.sub6resources.vampir.api.PredictionApi
import com.sub6resources.vampir.models.EncryptedCredentials

class PredictionRepository(private val predictionApi: PredictionApi) {
    fun predictBloodSugar(encryptedCredentials: EncryptedCredentials) = makeNetworkRequest(predictionApi.predictBloodSugar(encryptedCredentials)) {

    }
}