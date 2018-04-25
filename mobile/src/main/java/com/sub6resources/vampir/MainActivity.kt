package com.sub6resources.vampir

import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.PopupMenu
import com.firebase.jobdispatcher.Lifetime
import com.firebase.jobdispatcher.RetryStrategy
import com.firebase.jobdispatcher.Trigger
import com.sub6resources.utilities.*
import com.sub6resources.vampir.fragments.ChartFragment
import com.sub6resources.vampir.services.GlucoseMonitorService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(R.layout.activity_main) {
    override val drawer: DrawerLayout by lazy {drawer_layout}
    override val sideNav: NavigationView by lazy {side_nav}
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
            PopupMenu(this, btn_more).apply {
                setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.id.settings -> {
                            startActivity<SettingsActivity>()
                        }
                        R.id.legal -> {
                            //TODO legal
                        }
                        R.id.logout -> {
                            sharedPreferences.edit {
                                putString("token", "")
                                //TODO disconnect accounts
                                startActivity<LoginActivity>()
                            }
                        }
                    }
                    true
                }
                inflate(R.menu.navigation_main)
            }.show()
        }
    }
}
