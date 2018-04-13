package com.sub6resources.vampir

import com.sub6resources.utilities.BaseActivity
import com.sub6resources.vampir.fragments.LinkAccountsFragment

class LinkAccountsActivity: BaseActivity(R.layout.activity_fragmentcontainer) {
    override val fragmentTargets = R.id.fragment_container
    override val landingFragment = LinkAccountsFragment()
}