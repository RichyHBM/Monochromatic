package uk.co.richyhbm.monochromatic.Receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import uk.co.richyhbm.monochromatic.Services.MonochromeService


class MonochromeServiceRestartBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        MonochromeService.startService(context)
    }
}