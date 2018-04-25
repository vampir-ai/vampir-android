package com.sub6resources.vampir.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sub6resources.utilities.switchMap
import com.sub6resources.vampir.repository.AccountRepository
import com.sub6resources.vampir.models.Login

class LoginViewModel(accountRepository: AccountRepository): ViewModel() {

    private val credentials = MutableLiveData<Login>()
    val token = credentials.switchMap { accountRepository.login(it) }

    fun login(login: Login) {
        credentials.value = login
    }

}