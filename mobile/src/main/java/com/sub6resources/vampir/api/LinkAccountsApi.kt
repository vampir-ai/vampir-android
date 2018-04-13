package com.sub6resources.vampir.api

import com.sub6resources.vampir.models.CSRFResponse
import com.sub6resources.vampir.models.EncryptedCredentials
import com.sub6resources.vampir.models.Login
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface LinkAccountsApi {
    @POST("/api/encrypt/")
    fun encrypt(@Body login: Login): Single<EncryptedCredentials>

    @POST("/api/oauth/")
    fun OAuth(): Single<CSRFResponse>
}