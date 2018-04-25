package com.sub6resources.vampir.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sub6resources.utilities.switchMap
import com.sub6resources.vampir.repository.LinkAccountsRepository
import com.sub6resources.vampir.models.Login

class LinkAccountsViewModel(private val linkAccountsRepository: LinkAccountsRepository): ViewModel() {
    private val realtimeLogin = MutableLiveData<Login>()
    val realtimeEncryptedCredentials = realtimeLogin.switchMap { linkAccountsRepository.encryptLogin(it) }

    private val oAuthUnit = MutableLiveData<Unit>()
    val csrfResponse = oAuthUnit.switchMap { linkAccountsRepository.oAuth() }

    fun encrypt(login: Login) {
        realtimeLogin.value = login
    }

    fun oAuth() {
        oAuthUnit.value = Unit
    }
}