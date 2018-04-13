package com.sub6resources.vampir

import android.view.Gravity
import com.sub6resources.utilities.BaseActivity
import com.sub6resources.vampir.fragments.ChartFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(R.layout.activity_main) {
    override val drawer by lazy {drawer_layout}
    override val sideNav by lazy {side_nav}
    override val fragmentTargets = R.id.fragmentContainer
    override val landingFragment = ChartFragment()

    override fun setUp() {
        awesomebar_main.setOnMenuClickedListener {
            drawer.openDrawer(Gravity.START)
        }
    }
}
