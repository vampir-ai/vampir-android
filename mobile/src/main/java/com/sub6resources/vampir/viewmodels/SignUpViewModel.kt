package com.sub6resources.vampir.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sub6resources.utilities.switchMap
import com.sub6resources.vampir.repository.AccountRepository
import com.sub6resources.vampir.models.User

class SignUpViewModel(accountRepository: AccountRepository): ViewModel() {

    val signUpUser = MutableLiveData<User>()
    val signedUpUser = signUpUser.switchMap { accountRepository.signUp(it) }

    fun signUp(user: User) {
        signUpUser.value = user
    }
}