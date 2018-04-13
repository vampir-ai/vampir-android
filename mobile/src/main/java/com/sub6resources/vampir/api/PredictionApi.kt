package com.sub6resources.vampir.api

import com.sub6resources.vampir.models.EncryptedCredentials
import com.sub6resources.vampir.models.PredictionResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface PredictionApi {
    @POST("/api/predict/")
    fun predictBloodSugar(@Body encryptedCredentials: EncryptedCredentials): Single<PredictionResponse>
}