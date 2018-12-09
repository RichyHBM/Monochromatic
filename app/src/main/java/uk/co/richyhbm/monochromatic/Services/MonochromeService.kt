package uk.co.richyhbm.monochromatic.Services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import uk.co.richyhbm.monochromatic.Activities.MainActivity
import uk.co.richyhbm.monochromatic.R
import uk.co.richyhbm.monochromatic.Receivers.ScreenChangeReceiver
import uk.co.richyhbm.monochromatic.Utilities.SecureSettings
import uk.co.richyhbm.monochromatic.Utilities.Settings
import kotlin.random.Random


class MonochromeService : Service() {
    companion object {
        fun startService(context: Context) {
            val settings = Settings(context)
            if(settings.isEnabled() && !isRunning(context)) {
                val startServiceIntent = Intent(context, MonochromeService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(startServiceIntent)
                } else {
                    context.startService(startServiceIntent)
                }
            }
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
    private val channelId = "monochromatic_service"
    private val foregroundId = 1234

    override fun onCreate() {
        super.onCreate()
        screenChangeReceiver.registerReceiver(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = getString(R.string.app_name) + " service"
            val serviceChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_MIN)
            serviceChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET
            serviceChannel.setShowBadge(false)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(serviceChannel)
        }

    }

    override fun onDestroy() {
        SecureSettings.resetMonochrome(contentResolver)
        screenChangeReceiver.unregisterReceiver(this)
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val mainIntent = Intent(applicationContext, MainActivity::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingMainIntent = PendingIntent.getActivity(applicationContext, Random.nextInt(), mainIntent, 0)

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_filter_b_and_w_black)
            .setContentIntent(pendingMainIntent)
            .setOngoing(true)
            .setVisibility(Notification.VISIBILITY_SECRET)
            .setBadgeIconType(Notification.BADGE_ICON_NONE)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .build()

        startForeground(foregroundId, notification)

        if(!SecureSettings.isMonochromeEnabled(contentResolver)) {
            SecureSettings.toggleMonochrome(true, contentResolver)
        }

        return Service.START_STICKY
    }
}