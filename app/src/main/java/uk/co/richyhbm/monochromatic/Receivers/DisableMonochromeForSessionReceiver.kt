package uk.co.richyhbm.monochromatic.Receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import uk.co.richyhbm.monochromatic.Utilities.SecureSettings
import uk.co.richyhbm.monochromatic.Utilities.Settings

class DisableMonochromeForSessionReceiver : BroadcastReceiver() {
    companion object {
        fun disableForSession(context: Context) {
            val settings = Settings(context)
            if (settings.isEnabled()) {
                settings.sessionDisabled()
                SecureSettings.toggleFilters(settings.isAllowed(), context.contentResolver, settings)
            }
        }
    }

    override fun onReceive(context: Context?, p1: Intent?) {
        if (context != null) {
            disableForSession(context)
        }
    }
}