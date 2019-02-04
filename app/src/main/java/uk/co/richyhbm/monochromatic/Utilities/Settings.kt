package uk.co.richyhbm.monochromatic.Utilities

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.BatteryManager
import android.preference.PreferenceManager
import uk.co.richyhbm.monochromatic.R
import java.util.*


class Settings(val context: Context) {
    private val settings = PreferenceManager.getDefaultSharedPreferences(context)

    private fun getBoolean(keyId: Int, defaultValue: Boolean) =
        settings.getBoolean(context.getString(keyId), defaultValue)

    private fun getIntValue(keyId: Int, defaultValue: Int): Int {
        return try {
            settings.getInt(context.getString(keyId), defaultValue)
        } catch (e: Exception) {
            defaultValue
        }
    }

    private fun getStringSet(keyId: Int, defaultValue: MutableSet<String>): MutableSet<String> {
        val set = settings.getStringSet(context.getString(keyId), defaultValue)
        return set ?: defaultValue
    }


    private fun setBoolean(keyId: Int, value: Boolean) {
        settings.edit()
            .putBoolean(context.getString(keyId), value)
            .apply()
    }

    private fun setInt(keyId: Int, value: Int) {
        settings.edit()
            .putInt(context.getString(keyId), value)
            .apply()
    }

    private fun setStringSet(keyId: Int, value: Set<String>) {
        settings.edit()
            .putStringSet(context.getString(keyId), value)
            .apply()
    }

    fun registerPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        settings.registerOnSharedPreferenceChangeListener(listener)
    }

    fun isEnabled() =
        getBoolean(R.string.settings_key_monochromatic_enabled, false) && Permissions.hasSecureSettingsPermission(
            context
        )

    fun setEnabled(b: Boolean) {
        setBoolean(R.string.settings_key_monochromatic_enabled, b)
    }

    private fun isAlwaysOn() = getBoolean(R.string.settings_key_always_on, true)

    fun isFilterBluelightEnabled() = getBoolean(R.string.settings_key_bluelight_filter_enabled, false)

    fun shouldEnableAtTime() = getBoolean(R.string.settings_key_enable_with_time, false)

    fun setEnableTime(minutesAfterMidnight: Int) {
        return setInt(R.string.settings_key_enable_time, minutesAfterMidnight)
    }

    fun getEnableTime() = getIntValue(R.string.settings_key_enable_time, 0)

    fun setDisableTime(minutesAfterMidnight: Int) {
        return setInt(R.string.settings_key_disable_time, minutesAfterMidnight)
    }

    fun getDisableTime() = getIntValue(R.string.settings_key_disable_time, 0)

    private fun isNowInEnabledTime(): Boolean {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val nowTime = hour * 60 + minute
        return if (getDisableTime() < getEnableTime())
            getEnableTime() <= nowTime || nowTime < getDisableTime()
        else
            getEnableTime() <= nowTime && nowTime < getDisableTime()
    }

    private fun isTimeAllowed(): Boolean = shouldEnableAtTime() && isNowInEnabledTime()

    fun shouldEnableAtLowBattery() = getBoolean(R.string.settings_key_enable_with_low_battery, false)

    fun getLowBatteryLevel() = getIntValue(R.string.settings_key_enable_with_low_battery_amount, 15)

    fun setLowBatteryLevel(amount: Int) {
        setInt(R.string.settings_key_enable_with_low_battery_amount, amount)
    }

    private fun getBatteryLevel(): Int {
        val batteryIntent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED)) ?: return 50

        val level: Int = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale: Int = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

        return if (level == -1 || scale == -1) 50 else (level.toFloat() / scale.toFloat() * 100.0f).toInt()
    }

    private fun isBatteryAllowed(): Boolean = shouldEnableAtLowBattery() && (getBatteryLevel() <= getLowBatteryLevel())

    fun setBluelightFilterTemperature(amount: Int) {
        setInt(R.string.settings_key_bluelight_filter_temperature, amount)
    }

    fun getBluelightFilterTemperature(): Int =
        getIntValue(R.string.settings_key_bluelight_filter_temperature, Constants.defaultBluelightFilterTemperature)

    fun isAllowed(): Boolean {
        val allowed = isAlwaysOn() || isTimeAllowed() || isBatteryAllowed()

        if (!allowed)
            setBoolean(R.string.settings_key_disable_session, false)

        return allowed && !isQuickDisabled() && !isWhitelistDisabled()
    }

    fun setSeenNotificationDialog() {
        setBoolean(R.string.settings_key_show_notification_dialog, true)
    }

    fun seenNotificationDialog() =
        getBoolean(R.string.settings_key_show_notification_dialog, false)

    fun isQuickDisabled() = isScreenDisabled() || isSessionDisabled()

    private fun isScreenDisabled() = getBoolean(R.string.settings_key_disable_screen, false)

    private fun isSessionDisabled() = getBoolean(R.string.settings_key_disable_session, false)

    fun screenDisabled() = setBoolean(R.string.settings_key_disable_screen, true)
    fun resetScreenDisabled() = setBoolean(R.string.settings_key_disable_screen, false)

    fun sessionDisabled() = setBoolean(R.string.settings_key_disable_session, true)
    fun resetSessionDisabled() = setBoolean(R.string.settings_key_disable_session, false)

    fun isWhitelistDisabled() = getBoolean(R.string.settings_key_disable_whitelist, false)
    fun disableForScreenWhitelist() = setBoolean(R.string.settings_key_disable_whitelist, true)
    fun resetScreenWhitelistDisabled() = setBoolean(R.string.settings_key_disable_whitelist, false)

    fun isWhiteListed(packageName: String): Boolean {
        val set = getStringSet(R.string.settings_key_whitelisted_apps, mutableSetOf())
        return set.contains(packageName)
    }

    fun hasWhiteListedApps(): Boolean {
        val set = getStringSet(R.string.settings_key_whitelisted_apps, mutableSetOf())
        return !set.isEmpty()
    }

    fun addAppWhiteList(packageName: String) {
        val set = getStringSet(R.string.settings_key_whitelisted_apps, mutableSetOf())
        set.add(packageName)
        setStringSet(R.string.settings_key_whitelisted_apps, set)
    }

    fun removeAppWhiteList(packageName: String) {
        val set = getStringSet(R.string.settings_key_whitelisted_apps, mutableSetOf())
        set.remove(packageName)
        setStringSet(R.string.settings_key_whitelisted_apps, set)
    }

    fun clearAppWhiteListUninstalled(installedApps: Set<String>) {
        val set = getStringSet(R.string.settings_key_whitelisted_apps, mutableSetOf())
        set.removeAll { !installedApps.contains(it) }
        setStringSet(R.string.settings_key_whitelisted_apps, set)
    }
}
