package com.sub6resources.vampir

import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import com.firebase.jobdispatcher.Constraint
import com.firebase.jobdispatcher.Lifetime
import com.firebase.jobdispatcher.RetryStrategy
import com.firebase.jobdispatcher.Trigger
import com.sub6resources.utilities.BaseActivity
import com.sub6resources.utilities.onClick
import com.sub6resources.vampir.fragments.ChartFragment
import com.sub6resources.vampir.services.GlucoseMonitorService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(R.layout.activity_main) {
    override val drawer by lazy {drawer_layout}
    override val sideNav by lazy {side_nav}
    override val fragmentTargets = R.id.fragmentContainer
    override val landingFragment = ChartFragment()

    override val menu = R.menu.navigation_main

    override fun setUp() {

        val glucoseJob = (application as App).firebaseJobDispatcher.newJobBuilder().apply {
            setService(GlucoseMonitorService::class.java)
            tag = "GlucoseMonitorService"
            isRecurring = true
            lifetime = Lifetime.FOREVER
            trigger = Trigger.executionWindow(100, 200)
            setReplaceCurrent(true)
            retryStrategy = RetryStrategy.DEFAULT_EXPONENTIAL
        }.build()

        (application as App).firebaseJobDispatcher.mustSchedule(glucoseJob)

        btn_more.onClick {
            openOptionsMenu()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.settings -> {

            }
            R.id.legal -> {

            }
            R.id.logout -> {

            }
        }
        return super.onNavigationItemSelected(item)
    }
}
