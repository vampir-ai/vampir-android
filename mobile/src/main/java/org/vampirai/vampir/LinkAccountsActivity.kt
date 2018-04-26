package org.vampirai.vampir

import android.os.Bundle
import com.sub6resources.utilities.BaseActivity
import com.sub6resources.utilities.sharedPreferences
import com.sub6resources.utilities.startActivity
import org.vampirai.vampir.fragments.LinkAccountsFragment

class LinkAccountsActivity: BaseActivity(R.layout.activity_fragmentcontainer) {
    override val fragmentTargets = R.id.fragment_container
    override val landingFragment = LinkAccountsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        if(sharedPreferences.getString("encryptedRealtimeCredentials", "").isNotEmpty() && sharedPreferences.getBoolean("historicalLinked", false)) {
            startActivity<MainActivity>()
        }
        super.onCreate(savedInstanceState)
    }
}