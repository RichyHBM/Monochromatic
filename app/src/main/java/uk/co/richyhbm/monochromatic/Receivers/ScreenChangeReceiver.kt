package uk.co.richyhbm.monochromatic.Receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import uk.co.richyhbm.monochromatic.Utilities.SecureSettings
import uk.co.richyhbm.monochromatic.Utilities.Settings

class ScreenChangeReceiver : BroadcastReceiver() {
    fun registerReceiver(context: Context) {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        context.registerReceiver(this, filter)
    }

    fun unregisterReceiver(context: Context) {
        context.unregisterReceiver(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_SCREEN_ON -> screenOn(context)
            Intent.ACTION_SCREEN_OFF -> screenOff(context)
        }
    }

    private fun screenOn(context: Context) {
        val settings = Settings(context)

        if(settings.isEnabled()) {
            SecureSettings.toggleMonochrome(settings.isAllowed(), context.contentResolver)
        }
    }

    private fun screenOff(context: Context) {
        val settings = Settings(context)

        if(settings.isEnabled()) {
            if(settings.shouldDisableOnScreenOff()) {
                if(SecureSettings.isMonochromeEnabled(context.contentResolver)) {
                    SecureSettings.resetMonochrome(context.contentResolver)
                }
            } else {
                SecureSettings.toggleMonochrome(settings.isAllowed(), context.contentResolver)
            }
        }
    }
}

