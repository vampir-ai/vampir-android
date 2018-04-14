package com.sub6resources.vampir.repository

import com.sub6resources.vampir.api.LinkAccountsApi
import com.sub6resources.vampir.makeNetworkRequest
import com.sub6resources.vampir.models.Login

class LinkAccountsRepository(private val linkAccountsApi: LinkAccountsApi) {
    fun encryptLogin(login: Login) = makeNetworkRequest(linkAccountsApi.encrypt(login)) {

    }

    fun OAuth(unit: Unit) = makeNetworkRequest(linkAccountsApi.OAuth()) {
//        onError {
//
//        }
    }
}