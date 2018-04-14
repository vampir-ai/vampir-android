package com.sub6resources.vampir.repository

import com.sub6resources.vampir.BasicNetworkState
import com.sub6resources.vampir.api.AccountApi
import com.sub6resources.vampir.makeNetworkRequest
import com.sub6resources.vampir.models.Login
import com.sub6resources.vampir.models.User
import retrofit2.HttpException

class AccountRepository(val accountApi: AccountApi) {
    fun signUp(user: User) = makeNetworkRequest(accountApi.signUp(user)) {
        onError {
            if (it is HttpException) {
                if(it.code() == 400) {
                    BasicNetworkState.Error("Username already taken")
                } else {
                    BasicNetworkState.Error("Unknown error: " + it.code())
                }
            } else {
                BasicNetworkState.Error("Unknown Error")
            }
        }
    }

    fun login(login: Login) = makeNetworkRequest(accountApi.login(login)) {
        onError {
            if(it is HttpException) {
                if(it.code() == 401) {
                    BasicNetworkState.Error("Incorrect username or password")
                } else {
                    BasicNetworkState.Error("Unknown error: " + it.code())
                }
            } else {
                BasicNetworkState.Error("Unknown error")
            }
        }
    }
}