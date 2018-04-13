package com.sub6resources.vampir.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sub6resources.utilities.switchMap
import com.sub6resources.vampir.LinkAccountsRepository
import com.sub6resources.vampir.models.Login

class LinkAccountsViewModel(private val linkAccountsRepository: LinkAccountsRepository): ViewModel() {
    val realtimeLogin = MutableLiveData<Login>()
    val realtimeEncryptedCredentials = realtimeLogin.switchMap { linkAccountsRepository.encryptLogin(it) }

    fun encrypt(login: Login) {
        realtimeLogin.value = login
    }
}