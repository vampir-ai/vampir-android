package org.vampirai.vampir.api

import io.reactivex.Single
import org.vampirai.vampir.models.EncryptedCredentials
import org.vampirai.vampir.models.PredictionResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface PredictionApi {
    @POST("/api/predict/")
    fun predictBloodSugar(@Body encryptedCredentials: EncryptedCredentials): Single<PredictionResponse>
}