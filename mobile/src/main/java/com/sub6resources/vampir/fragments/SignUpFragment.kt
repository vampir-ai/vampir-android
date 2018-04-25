package com.sub6resources.vampir.fragments

import android.arch.lifecycle.Observer
import com.sub6resources.utilities.*
import com.sub6resources.vampir.BasicNetworkState
import com.sub6resources.vampir.LinkAccountsActivity
import com.sub6resources.vampir.R
import com.sub6resources.vampir.models.Login
import com.sub6resources.vampir.models.User
import com.sub6resources.vampir.viewmodels.LoginViewModel
import com.sub6resources.vampir.viewmodels.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_signup.*
import java.util.*

class SignUpFragment: BaseFragment() {
    override val fragLayout = R.layout.fragment_signup

    private val signUpViewModel by getViewModel<SignUpViewModel>()
    private val loginViewModel by getViewModel<LoginViewModel>()

    override fun setUp() {

        login_text.onClick {
            addFragment(LoginFragment())
        }

        btn_signup.onClick {
            if(et_password.getString() == et_password_confirm.getString()) {
                signUpViewModel.signUp(User(UUID.randomUUID(), et_username.getString(), "Hardcoded", "User", et_password.getString(), ""))
            } else {
                et_password_confirm.error = "Passwords do not match"
            }

        }

        val loadingDialog = baseActivity.dialog {
            progress(true, 0)
            content("Loading...")
        }

        signUpViewModel.signedUpUser.observe(this, Observer {
            when(it) {
                is BasicNetworkState.Success -> {
                    loginViewModel.login(Login(et_username.getString(), et_password.getString()))

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
                    loadingDialog.dismiss()
                }
                is BasicNetworkState.Error -> {
                    et_password_confirm.error = it.message
                    loadingDialog.dismiss()
                }
                is BasicNetworkState.Loading -> {
                    loadingDialog.show()
                }
            }
        })
    }
}