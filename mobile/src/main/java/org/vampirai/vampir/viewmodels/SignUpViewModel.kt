package org.vampirai.vampir.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sub6resources.utilities.switchMap
import org.vampirai.vampir.models.User
import org.vampirai.vampir.repository.AccountRepository

class SignUpViewModel(accountRepository: AccountRepository): ViewModel() {

    private val signUpUser = MutableLiveData<User>()
    val signedUpUser = signUpUser.switchMap { accountRepository.signUp(it) }

    fun signUp(user: User) {
        signUpUser.value = user
    }
}