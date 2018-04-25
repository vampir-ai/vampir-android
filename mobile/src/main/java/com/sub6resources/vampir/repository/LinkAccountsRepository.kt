package com.sub6resources.vampir.repository

import com.sub6resources.vampir.BasicNetworkState
import com.sub6resources.vampir.api.LinkAccountsApi
import com.sub6resources.vampir.makeNetworkRequest
import com.sub6resources.vampir.models.Login
import retrofit2.HttpException

class LinkAccountsRepository(private val linkAccountsApi: LinkAccountsApi) {
    fun encryptLogin(login: Login) = makeNetworkRequest(linkAccountsApi.encrypt(login)) {
        onError {
            if(it is HttpException) {
                if(it.code() == 401) {
                    BasicNetworkState.Error("401 - Not authenticated")
                } else {
                    BasicNetworkState.Error("Unknown error: ${it.code()}")
                }
            } else {
                BasicNetworkState.Error("Unknown error: ${it.message}")
            }
        }
    }

    fun oAuth() = makeNetworkRequest(linkAccountsApi.oAuth()) {
        onError {
            if(it is HttpException) {
                if(it.code() == 401) {
                    BasicNetworkState.Error("401 - Not authenticated")
                } else {
                    BasicNetworkState.Error("Unknown error: ${it.code()}")
                }
            } else {
                BasicNetworkState.Error("Unknown error: ${it.message}")
            }
        }
    }
}