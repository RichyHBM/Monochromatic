package uk.co.richyhbm.monochromatic.Receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class ScreenOffReceiver : BroadcastReceiver() {

    companion object {
        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
             Intent.ACTION_SCREEN_OFF -> resetMonochrome()
        }
    }

    private fun resetMonochrome() {

    }
}

