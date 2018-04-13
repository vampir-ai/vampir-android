package com.sub6resources.vampir.fragments

import android.arch.lifecycle.Observer
import com.sub6resources.utilities.*
import com.sub6resources.vampir.BasicNetworkState
import com.sub6resources.vampir.MainActivity
import com.sub6resources.vampir.R
import com.sub6resources.vampir.viewmodels.LinkAccountsViewModel
import kotlinx.android.synthetic.main.fragment_linkaccounts.*

class LinkAccountsFragment: BaseFragment() {
    override val fragLayout = R.layout.fragment_linkaccounts

    val linkAccountsViewModel by lazy { getSharedViewModel<LinkAccountsViewModel>() }

    override fun setUp() {


        btn_linkhistorical.onClick {
//            baseActivity.sharedPreferences.edit {
//                putBoolean("historicalLinked", false)
//            }
            //TODO Remove in PROD
//            baseActivity.startActivity<MainActivity>()
            addFragment(LinkHistoricalFragment())
        }

        btn_linkrealtime.onClick {
            addFragment(LinkRealtimeFragment())
        }




    }

    override fun onResume() {
        super.onResume()
        if(baseActivity.sharedPreferences.getString("encryptedRealtimeCredentials", "").isNotEmpty()) {
            realtime_linked_checkbox.isChecked = true
            btn_linkrealtime.disable()
            btn_linkrealtime.text = "Realtime Account Linked"
        } else {
            realtime_linked_checkbox.isChecked = false
            btn_linkrealtime.enable()
            btn_linkrealtime.text = "Link Realtime"
        }

        if(baseActivity.sharedPreferences.getBoolean("historicalLinked", false)) {
            historical_linked_checkbox.isChecked = true
            btn_linkhistorical.disable()
            btn_linkhistorical.text = "Historical Account Linked"
        } else {
            historical_linked_checkbox.isChecked = false
            btn_linkhistorical.enable()
            btn_linkhistorical.text = "Link Historical"
        }

    }
}