package com.sub6resources.vampir

import com.sub6resources.vampir.api.LinkAccountsApi
import com.sub6resources.vampir.models.EncryptedCredentials
import com.sub6resources.vampir.models.Login

class LinkAccountsRepository(private val linkAccountsApi: LinkAccountsApi) {
    fun encryptLogin(login: Login) = makeNetworkRequest(linkAccountsApi.encrypt(login)) {

    }
}