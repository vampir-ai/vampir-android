package org.vampirai.vampir.fragments

import android.arch.lifecycle.Observer
import com.sub6resources.utilities.*
import kotlinx.android.synthetic.main.fragment_linkrealtime.*
import org.vampirai.vampir.BasicNetworkState
import org.vampirai.vampir.models.Login
import org.vampirai.vampir.viewmodels.LinkAccountsViewModel
import org.vampirai.vampir.viewmodels.PredictionViewModel
import org.vampirai.vampir.R

class LinkRealtimeFragment: BaseFragment() {
    override val fragLayout = R.layout.fragment_linkrealtime

    private val linkAccountsViewModel by lazy { getSharedViewModel<LinkAccountsViewModel>() }
    private val predictionViewModel by getViewModel<PredictionViewModel>()

    override fun setUp() {

        btn_login.onClick {
            linkAccountsViewModel.encrypt(Login(et_dexcom_username.getString(), et_dexcom_password.getString()))
        }

        val loadingDialog = baseActivity.dialog {
            progress(true, 0)
            content("Loading...")
        }

        linkAccountsViewModel.realtimeEncryptedCredentials.observe(this, Observer { credentials ->
            when(credentials) {
                is BasicNetworkState.Success -> {

                    loadingDialog.setContent("Validating Credentials...")

                    predictionViewModel.predict(credentials.data)
                    predictionViewModel.predictedData.observe(this, Observer {
                        when(it) {
                            is BasicNetworkState.Success -> {
                                baseActivity.sharedPreferences.edit {
                                    putString("encryptedRealtimeCredentials", credentials.data.encryptedCredentials)
                                }
                                popFragment()
                                loadingDialog.dismiss()
                            }
                            is BasicNetworkState.Error -> {
                                et_dexcom_password.error = it.message
                                loadingDialog.dismiss()
                            }
                            is BasicNetworkState.Loading -> {
                                loadingDialog.show()
                            }
                        }
                    })



                }
                is BasicNetworkState.Error -> {
                    et_dexcom_password.error = credentials.message
                    loadingDialog.dismiss()
                }
                is BasicNetworkState.Loading -> {
                    loadingDialog.show()
                }
            }
        })

    }
}