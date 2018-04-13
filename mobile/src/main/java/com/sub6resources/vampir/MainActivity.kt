package com.sub6resources.vampir

import com.sub6resources.utilities.BaseActivity
import com.sub6resources.vampir.fragments.ChartFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(R.layout.activity_main) {
    override val drawer = drawer_layout
    override val sideNav = side_nav
    override val fragmentTargets = R.id.fragmentContainer
    override val landingFragment = ChartFragment()
}
