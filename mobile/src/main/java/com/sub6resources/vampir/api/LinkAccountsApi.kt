package com.sub6resources.vampir.api

import com.sub6resources.vampir.models.EncryptedCredentials
import com.sub6resources.vampir.models.Login
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface LinkAccountsApi {
    @POST
    fun encrypt(@Body login: Login): Single<EncryptedCredentials>
}