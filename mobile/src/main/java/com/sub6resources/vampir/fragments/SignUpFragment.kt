package com.sub6resources.vampir.fragments

import android.arch.lifecycle.Observer
import com.sub6resources.utilities.BaseFragment
import com.sub6resources.utilities.dialog
import com.sub6resources.utilities.getString
import com.sub6resources.utilities.onClick
import com.sub6resources.vampir.BasicNetworkState
import com.sub6resources.vampir.R
import com.sub6resources.vampir.models.User
import com.sub6resources.vampir.viewmodels.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_signup.*
import java.util.*

class SignUpFragment: BaseFragment() {
    override val fragLayout = R.layout.fragment_signup

    val signUpViewModel by getViewModel<SignUpViewModel>()

    override fun setUp() {

        login_text.onClick {
            addFragment(LoginFragment())
        }

        btn_signup.onClick {
            signUpViewModel.signUp(User(UUID.randomUUID(), et_username.getString(), "Hardcoded", "User", et_password.getString(), ""))
        }

        val loadingDialog = baseActivity.dialog {
            progress(true, 0)
            content("Loading...")
        }

        signUpViewModel.signedUpUser.observe(this, Observer {
            when(it) {
                is BasicNetworkState.Success -> {
                    switchFragment(LoginFragment())
                    loadingDialog.dismiss()
                }
                is BasicNetworkState.Error -> {
                    et_password.error = it.message
                    loadingDialog.dismiss()
                }
                is BasicNetworkState.Loading -> {
                    loadingDialog.show()
                }
            }
        })
    }
}