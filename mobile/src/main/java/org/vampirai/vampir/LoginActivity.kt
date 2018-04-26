package org.vampirai.vampir

import android.os.Bundle
import com.sub6resources.utilities.BaseActivity
import com.sub6resources.utilities.sharedPreferences
import com.sub6resources.utilities.startActivity
import org.vampirai.vampir.fragments.SignUpFragment

class LoginActivity: BaseActivity(R.layout.activity_fragmentcontainer) {
    override val fragmentTargets = R.id.fragment_container
    override val landingFragment = SignUpFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        if(sharedPreferences.getString("token", "").isNotEmpty()) {
            startActivity<LinkAccountsActivity>()
        }
        super.onCreate(savedInstanceState)
    }
}