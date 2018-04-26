package org.vampirai.vampir.repository

import org.vampirai.vampir.BasicNetworkState
import org.vampirai.vampir.api.LinkAccountsApi
import org.vampirai.vampir.makeNetworkRequest
import org.vampirai.vampir.models.Login
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