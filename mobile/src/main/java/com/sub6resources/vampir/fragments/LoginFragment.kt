package com.sub6resources.vampir.fragments

import android.arch.lifecycle.Observer
import com.sub6resources.utilities.*
import com.sub6resources.vampir.BasicNetworkState
import com.sub6resources.vampir.LinkAccountsActivity
import com.sub6resources.vampir.R
import com.sub6resources.vampir.models.Login
import com.sub6resources.vampir.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment: BaseFragment() {
    override val fragLayout = R.layout.fragment_login

    private val loginViewModel by getViewModel<LoginViewModel>()

    override fun setUp() {
        btn_login.onClick {
            loginViewModel.login(Login(et_username.getString(), et_password.getString()))
        }

        val loadingDialog = baseActivity.dialog {
            progress(true, 0)
            content("Loading...")
        }

        loginViewModel.token.observe(this, Observer {
            when(it) {
                is BasicNetworkState.Success -> {
                    baseActivity.sharedPreferences.edit {
                        putString("token", it.data.token)
                    }
                    baseActivity.startActivity<LinkAccountsActivity>()
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