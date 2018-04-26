package org.vampirai.vampir.api

import io.reactivex.Single
import org.vampirai.vampir.models.Login
import org.vampirai.vampir.models.Token
import org.vampirai.vampir.models.User
import retrofit2.http.Body
import retrofit2.http.POST

interface AccountApi {
    @POST("/api/sign-up/")
    fun signUp(@Body user: User): Single<User>

    @POST("/api-token-auth/")
    fun login(@Body login: Login): Single<Token>
}