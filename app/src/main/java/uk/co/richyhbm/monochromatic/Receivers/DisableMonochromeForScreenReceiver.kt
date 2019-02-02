package uk.co.richyhbm.monochromatic.Receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import uk.co.richyhbm.monochromatic.Utilities.SecureSettings
import uk.co.richyhbm.monochromatic.Utilities.Settings

class DisableMonochromeForScreenReceiver : BroadcastReceiver() {
    companion object {
        fun disableForScreen(context: Context) {
            val settings = Settings(context)
            if (settings.isEnabled()) {
                settings.screenDisabled()
                SecureSettings.toggleFilters(settings.isAllowed(), context.contentResolver, settings)
            }
        }
    }

    override fun onReceive(context: Context?, p1: Intent?) {
        if (context != null) {
            disableForScreen(context)
        }
    }
}