package org.vampirai.vampir.fragments

import android.arch.lifecycle.Observer
import com.sub6resources.utilities.*
import kotlinx.android.synthetic.main.fragment_login.*
import org.vampirai.vampir.BasicNetworkState
import org.vampirai.vampir.LinkAccountsActivity
import org.vampirai.vampir.models.Login
import org.vampirai.vampir.viewmodels.LoginViewModel
import org.vampirai.vampir.R

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