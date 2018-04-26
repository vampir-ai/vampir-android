package org.vampirai.vampir.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sub6resources.utilities.switchMap
import org.vampirai.vampir.models.Login
import org.vampirai.vampir.repository.AccountRepository

class LoginViewModel(accountRepository: AccountRepository): ViewModel() {

    private val credentials = MutableLiveData<Login>()
    val token = credentials.switchMap { accountRepository.login(it) }

    fun login(login: Login) {
        credentials.value = login
    }

}