package uk.co.richyhbm.monochromatic.Receivers

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import uk.co.richyhbm.monochromatic.Utilities.Permissions
import uk.co.richyhbm.monochromatic.Utilities.SecureSettings
import uk.co.richyhbm.monochromatic.Utilities.Settings
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class ScreenChangeReceiver : BroadcastReceiver() {
    var scheduler: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    fun registerReceiver(context: Context) {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        context.registerReceiver(this, filter)

        if(scheduler.isShutdown) {
            scheduler = Executors.newSingleThreadScheduledExecutor()
        }

        val settings = Settings(context)
        if(!scheduler.isShutdown) {
            scheduler.scheduleAtFixedRate({
                if (!settings.isQuickDisabled()) {
                    if (isForegroundAppWhitelisted(context)) {
                        settings.disableForScreenWhitelist()
                        SecureSettings.toggleFilters(settings.isAllowed(), context.contentResolver, settings)
                    } else {
                        settings.resetScreenWhitelistDisabled()
                        SecureSettings.toggleFilters(settings.isAllowed(), context.contentResolver, settings)
                    }
                }
            }, 0, 1, TimeUnit.SECONDS)
        }
    }

    fun unregisterReceiver(context: Context) {
        scheduler.shutdown()
        context.unregisterReceiver(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val settings = Settings(context)
        settings.resetScreenDisabled()

        when (intent.action) {
            Intent.ACTION_SCREEN_ON -> screenOn(context)
            Intent.ACTION_SCREEN_OFF -> screenOff(context)
        }
    }

    private fun screenOn(context: Context) {
        val settings = Settings(context)

        if(settings.isEnabled()) {
            SecureSettings.toggleFilters(settings.isAllowed(), context.contentResolver, settings)
            if (settings.hasWhiteListedApps() && Permissions.hasUsageStatsPermission(context)) {
                if(scheduler.isShutdown) {
                    scheduler = Executors.newSingleThreadScheduledExecutor()
                }

                if(!scheduler.isShutdown) {
                    scheduler.scheduleAtFixedRate({
                        if (!settings.isQuickDisabled()) {
                            if (isForegroundAppWhitelisted(context)) {
                                settings.disableForScreenWhitelist()
                                SecureSettings.toggleFilters(settings.isAllowed(), context.contentResolver, settings)
                            } else {
                                settings.resetScreenWhitelistDisabled()
                                SecureSettings.toggleFilters(settings.isAllowed(), context.contentResolver, settings)
                            }
                        }
                    }, 0, 1, TimeUnit.SECONDS)
                }
            }
        }
    }

    private fun screenOff(context: Context) {
        val settings = Settings(context)

        if(settings.isEnabled()) {
            SecureSettings.toggleFilters(settings.isAllowed(), context.contentResolver, settings)
            scheduler.shutdown()
        }
    }

    private fun isForegroundAppWhitelisted(context: Context): Boolean {
        val uss = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Context.USAGE_STATS_SERVICE
        } else {
            "usagestats"
        }

        val usageSettings = context.getSystemService(uss) as UsageStatsManager
        val time = System.currentTimeMillis()
        val events = usageSettings.queryEvents(time - 1000 * 10, time)
        val event: UsageEvents.Event = UsageEvents.Event()

        var foregroundApp = ""
        while(events.getNextEvent(event)) {
            if(event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                foregroundApp = event.packageName
            }
        }

        return Settings(context).isWhiteListed(foregroundApp)
    }
}

