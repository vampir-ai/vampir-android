package org.vampirai.vampir.repository

import org.vampirai.vampir.BasicNetworkState
import org.vampirai.vampir.api.AccountApi
import org.vampirai.vampir.makeNetworkRequest
import org.vampirai.vampir.models.Login
import org.vampirai.vampir.models.User
import retrofit2.HttpException

class AccountRepository(val accountApi: AccountApi) {
    fun signUp(user: User) = makeNetworkRequest(accountApi.signUp(user)) {
        onError {
            if (it is HttpException) {
                if(it.code() == 400) {
                    BasicNetworkState.Error("Username already taken")
                } else {
                    BasicNetworkState.Error("Unknown error: ${it.code()}")
                }
            } else {
                BasicNetworkState.Error("Unknown Error: ${it.message}")
            }
        }
    }

    fun login(login: Login) = makeNetworkRequest(accountApi.login(login)) {
        onError {
            if(it is HttpException) {
                if(it.code() == 401) {
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