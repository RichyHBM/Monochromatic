package uk.co.richyhbm.monochromatic.Receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import uk.co.richyhbm.monochromatic.Utilities.Permissions
import uk.co.richyhbm.monochromatic.Utilities.SecureSettings
import uk.co.richyhbm.monochromatic.Utilities.Settings



class BatteryReceiver : BroadcastReceiver() {
    fun registerReceiver(context: Context) {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(this, filter)
    }

    fun unregisterReceiver(context: Context) {
        context.unregisterReceiver(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val settings = Settings(context)

        if(settings.shouldEnableAtLowBattery() && Permissions.hasSecureSettingsPermission(context)) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)

            if(level <= settings.getLowBatteryLevel()) {
                SecureSettings.toggleMonochrome(true, context.contentResolver)
            } else {
                SecureSettings.toggleMonochrome(settings.isTimeAllowed(), context.contentResolver)
            }
        }
    }
}