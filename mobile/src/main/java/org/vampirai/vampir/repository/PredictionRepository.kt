package org.vampirai.vampir.repository

import org.vampirai.vampir.BasicNetworkState
import org.vampirai.vampir.api.PredictionApi
import org.vampirai.vampir.makeNetworkRequest
import org.vampirai.vampir.models.EncryptedCredentials
import retrofit2.HttpException

class PredictionRepository(private val predictionApi: PredictionApi) {
    fun predictBloodSugar(encryptedCredentials: EncryptedCredentials) = makeNetworkRequest(predictionApi.predictBloodSugar(encryptedCredentials)) {
        onError {
            if(it is HttpException) {
                if(it.code() == 401 || it.code() == 500) {
                    BasicNetworkState.Error("Incorrect username or password")
                } else {
                    BasicNetworkState.Error("Unknown error: ${it.code()}")
                }
            } else {
                BasicNetworkState.Error("Unknown error: ${it.message}")
            }
        }
    }
}