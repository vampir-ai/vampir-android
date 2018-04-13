package com.sub6resources.vampir

import com.sub6resources.vampir.api.AccountApi
import com.sub6resources.vampir.models.Login
import com.sub6resources.vampir.models.User

class AccountRepository(val accountApi: AccountApi) {
    fun signUp(user: User) = makeNetworkRequest(accountApi.signUp(user)) {

    }

    fun login(login: Login) = makeNetworkRequest(accountApi.login(login)) {

    }
}