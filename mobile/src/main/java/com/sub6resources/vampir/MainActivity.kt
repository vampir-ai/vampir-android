package com.sub6resources.vampir

import android.view.Gravity
import com.firebase.jobdispatcher.Constraint
import com.firebase.jobdispatcher.Lifetime
import com.firebase.jobdispatcher.RetryStrategy
import com.firebase.jobdispatcher.Trigger
import com.sub6resources.utilities.BaseActivity
import com.sub6resources.vampir.fragments.ChartFragment
import com.sub6resources.vampir.services.GlucoseMonitorService
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

        val glucoseJob = (application as App).firebaseJobDispatcher.newJobBuilder().apply {
            setService(GlucoseMonitorService::class.java)
            tag = "GlucoseMonitorService"
            isRecurring = true
            lifetime = Lifetime.FOREVER
            trigger = Trigger.executionWindow(200, 300)
            setReplaceCurrent(false)
            retryStrategy = RetryStrategy.DEFAULT_EXPONENTIAL
            setConstraints(Constraint.ON_ANY_NETWORK)
        }.build()

        (application as App).firebaseJobDispatcher.mustSchedule(glucoseJob)
    }
}
