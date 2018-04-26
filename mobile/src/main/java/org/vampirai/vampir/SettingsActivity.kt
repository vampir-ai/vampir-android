package org.vampirai.vampir

import com.sub6resources.utilities.BaseSettingsActivity
import com.sub6resources.utilities.group
import com.sub6resources.utilities.setting
import com.sub6resources.utilities.settingsActivity

class SettingsActivity: BaseSettingsActivity() {
    override val settings = settingsActivity {
        group("Notification Settings") {
            setting("notifyLow", 50) {
                description = "Notify if glucose levels fall below"
                units = "mg/dL"
            }
            setting("notifyHigh", 300) {
                description = "Notify if glucose levels rise above"
                units = "mg/dL"
            }
            setting("persistentNotification", false) {
                description = "Show a persistent notification with current glucose level"
                subtitleIfTrue = "Notification will be shown"
                subtitleIfFalse = "Notification will not be shown"
            }
        }
    }
}