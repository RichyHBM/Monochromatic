package uk.co.richyhbm.monochromatic.Utilities

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import uk.co.richyhbm.monochromatic.R
import java.util.*


class Settings(val context: Context) {
    private val settings = PreferenceManager.getDefaultSharedPreferences(context)

    private fun getString(keyId: Int, defaultValue: String): String {
        return settings.getString(context.getString(keyId), defaultValue) ?: ""
    }

    private fun getBoolean(keyId: Int, defaultValue: Boolean): Boolean {
        return settings.getBoolean(context.getString(keyId), defaultValue)
    }

    private fun getIntValue(keyId: Int, defaultValue: Int): Int {
        return settings.getInt(context.getString(keyId), defaultValue)
    }

    private fun getLong(keyId: Int, defaultValue: Long): Long {
        return settings.getLong(context.getString(keyId), defaultValue)
    }

    private fun getStringSet(keyId: Int, defaultValue: Set<String>): HashSet<String> {
        return HashSet(settings.getStringSet(context.getString(keyId), defaultValue))
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

    private fun setString(keyId: Int, value: String) {
        settings.edit()
            .putString(context.getString(keyId), value)
            .apply()
    }

    private fun setStringSet(keyId: Int, value: Set<String>) {
        settings.edit()
            .putStringSet(context.getString(keyId), value)
            .apply()
    }

    private fun remove(keyId: Int) {
        settings.edit()
            .remove(context.getString(keyId))
            .apply()
    }

    fun registerPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        settings.registerOnSharedPreferenceChangeListener(listener)
    }

    fun isEnabled(): Boolean {
        return getBoolean(R.string.settings_key_monochromatic_enabled, false) && Permissions.hasSecureSettingsPermission(context)
    }

    fun setEnabled(b: Boolean) {
        setBoolean(R.string.settings_key_monochromatic_enabled, b)
    }

    fun shouldDisableOnScreenOff(): Boolean {
        return getBoolean(R.string.settings_key_disable_with_screen_off, false)
    }

    fun shouldEnableAtTime(): Boolean {
        return getBoolean(R.string.settings_key_enable_with_time, false)
    }

    fun setEnableTime(minutesAfterMidnight: Int) {
        return setInt(R.string.settings_key_enable_time, minutesAfterMidnight)
    }

    fun getEnableTime() : Int {
        return getIntValue(R.string.settings_key_enable_time, 0)
    }

    fun setDisableTime(minutesAfterMidnight: Int) {
        return setInt(R.string.settings_key_disable_time, minutesAfterMidnight)
    }

    fun getDisableTime() : Int {
        return getIntValue(R.string.settings_key_disable_time, 0)
    }

    fun isNowInEnabledTime(): Boolean {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val nowTime = hour * 60 + minute
        return if(getDisableTime() < getEnableTime())
            getEnableTime() < nowTime || nowTime < getDisableTime()
        else
            getEnableTime() < nowTime && nowTime < getDisableTime()
    }

    fun isTimeAllowed(): Boolean = !shouldEnableAtTime() || (shouldEnableAtTime() && isNowInEnabledTime())
}