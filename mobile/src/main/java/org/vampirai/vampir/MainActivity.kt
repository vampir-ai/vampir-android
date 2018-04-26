package org.vampirai.vampir

import android.support.v7.widget.PopupMenu
import com.firebase.jobdispatcher.Lifetime
import com.firebase.jobdispatcher.RetryStrategy
import com.firebase.jobdispatcher.Trigger
import com.sub6resources.utilities.*
import kotlinx.android.synthetic.main.activity_main.*
import org.vampirai.vampir.fragments.ChartFragment
import org.vampirai.vampir.services.GlucoseMonitorService

class MainActivity : BaseActivity(R.layout.activity_main) {
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
                            startActivity<LegalActivity>()
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
