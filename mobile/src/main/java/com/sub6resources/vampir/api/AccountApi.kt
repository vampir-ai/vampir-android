package com.sub6resources.vampir.api

import com.sub6resources.vampir.models.Login
import com.sub6resources.vampir.models.Token
import com.sub6resources.vampir.models.User
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface AccountApi {
    @POST("/api/sign-up/")
    fun signUp(@Body user: User): Single<User>

    @POST("/api-token-auth/")
    fun login(@Body login: Login): Single<Token>
}