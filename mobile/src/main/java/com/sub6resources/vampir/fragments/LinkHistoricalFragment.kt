package com.sub6resources.vampir.fragments

import android.arch.lifecycle.Observer
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.sub6resources.utilities.BaseFragment
import com.sub6resources.utilities.dialog
import com.sub6resources.vampir.BasicNetworkState
import com.sub6resources.vampir.R
import com.sub6resources.vampir.models.CSRFResponse
import com.sub6resources.vampir.viewmodels.LinkAccountsViewModel
import kotlinx.android.synthetic.main.fragment_linkhistorical.*

class LinkHistoricalFragment: BaseFragment() {

    override val fragLayout = R.layout.fragment_linkhistorical

    val linkAccountsViewModel by lazy { getSharedViewModel<LinkAccountsViewModel>() }

    override fun setUp() {

        val loadingDialog = baseActivity.dialog {
            progress(true, 0)
            content("Loading...")
        }
        loadingDialog.show()

        linkAccountsViewModel.OAuth()

        linkAccountsViewModel.csrfResponse.observe(this, Observer {
            when(it) {
                is BasicNetworkState.Success -> {
                    loadingDialog.setContent("Loading Webpage...")
                    loadUrl(it.data)
                }
                is BasicNetworkState.Error -> {
                    loadingDialog.dismiss()
                    baseActivity.dialog {
                        title("Error")
                        content("There was an error connecting to the server\n\n${it.message}")
                    }.show()
                }
                is BasicNetworkState.Loading -> {
                    //Already showing loading dialog
                }
            }
        })

        oauth_webview.settings.javaScriptEnabled = true
        oauth_webview.webViewClient = object: WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                loadingDialog.dismiss()
                if(url.contains("ryanberger.me")) {
                    Log.d("HOORAY", "It works!!! $url")
                }
                super.onPageFinished(view, url)
            }
        }
    }

    fun loadUrl(csrf: CSRFResponse) {
        oauth_webview.loadUrl("https://api.dexcom.com/v1/oauth2/login?client_id=Nqsx3FAK4N0xLcDvkQH0ACFhuMNS86Rb&redirect_uri=http://vampirai.ryanberger.me/api/oauth/&response_type=code&scope=offline_access&state=${csrf.csrf}")
    }
}