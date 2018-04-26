package org.vampirai.vampir

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import com.sub6resources.vampir.R

class MainActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
        setAmbientEnabled()
    }
}
