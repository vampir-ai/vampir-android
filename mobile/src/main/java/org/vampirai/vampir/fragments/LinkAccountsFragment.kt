package org.vampirai.vampir.fragments

import com.sub6resources.utilities.*
import kotlinx.android.synthetic.main.fragment_linkaccounts.*
import org.vampirai.vampir.MainActivity
import org.vampirai.vampir.R

class LinkAccountsFragment: BaseFragment() {
    override val fragLayout = R.layout.fragment_linkaccounts

    override fun setUp() {


        btn_linkhistorical.onClick {
            addFragment(LinkHistoricalFragment())
        }

        btn_linkrealtime.onClick {
            addFragment(LinkRealtimeFragment())
        }

        txt_nodexcom.onClick {
            baseActivity.sharedPreferences.edit {
                putString("encryptedRealtimeCredentials", "---")
                putBoolean("historicalLinked", true)
            }

            baseActivity.startActivity<MainActivity>()
        }

        checkConnections()


    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        checkConnections()
    }

    fun checkConnections() {
        if(baseActivity.sharedPreferences.getString("encryptedRealtimeCredentials", "").isNotEmpty()) {
            realtime_linked_checkbox.isChecked = true
            btn_linkrealtime.disable()
            btn_linkrealtime.text = getString(R.string.realtime_linked)
        } else {
            realtime_linked_checkbox.isChecked = false
            btn_linkrealtime.enable()
            btn_linkrealtime.text = getString(R.string.link_realtime)
        }

        if(!baseActivity.sharedPreferences.getBoolean("historicalLinked", false)) {
            historical_linked_checkbox.isChecked = true
            btn_linkhistorical.disable()
            btn_linkhistorical.text = getString(R.string.historical_linked)
        } else {
            historical_linked_checkbox.isChecked = false
            btn_linkhistorical.enable()
            btn_linkhistorical.text = getString(R.string.link_historical)
        }

        if(baseActivity.sharedPreferences.getString("encryptedRealtimeCredentials", "").isNotEmpty() && !baseActivity.sharedPreferences.getBoolean("historicalLinked", false)) {
            baseActivity.startActivity<MainActivity>()
        }
    }
}