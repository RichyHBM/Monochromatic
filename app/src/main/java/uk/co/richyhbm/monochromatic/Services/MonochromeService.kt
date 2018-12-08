package uk.co.richyhbm.monochromatic.Services

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import uk.co.richyhbm.monochromatic.Receivers.ScreenChangeReceiver



class MonochromeService : Service() {
    companion object {
        fun startService(context: Context) {
            val startServiceIntent = Intent(context, MonochromeService::class.java)
            context.startService(startServiceIntent)
        }

        fun stopService(context: Context) {
            val stopServiceIntent = Intent(context, MonochromeService::class.java)
            context.stopService(stopServiceIntent)
        }

        @Suppress("DEPRECATION")
        fun isRunning(context: Context) : Boolean {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
                if (MonochromeService::class.java.name == service.service.className) {
                    return true
                }
            }
            return false
        }
    }

    private val screenChangeReceiver = ScreenChangeReceiver()

    override fun onCreate() {
        super.onCreate()
        screenChangeReceiver.registerReceiver(this)
    }

    override fun onDestroy() {
        screenChangeReceiver.unregisterReceiver(this)
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return Service.START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }


}