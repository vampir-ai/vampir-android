package org.vampirai.vampir.api

import io.reactivex.Single
import org.vampirai.vampir.models.CSRFResponse
import org.vampirai.vampir.models.EncryptedCredentials
import org.vampirai.vampir.models.Login
import retrofit2.http.Body
import retrofit2.http.POST

interface LinkAccountsApi {
    @POST("/api/encrypt/")
    fun encrypt(@Body login: Login): Single<EncryptedCredentials>

    @POST("/api/oauth/")
    fun oAuth(): Single<CSRFResponse>
}