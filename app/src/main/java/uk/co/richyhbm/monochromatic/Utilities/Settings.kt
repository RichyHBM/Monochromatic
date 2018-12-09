package uk.co.richyhbm.monochromatic.Utilities

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import uk.co.richyhbm.monochromatic.R



class Settings(val context: Context) {
    private val settings = PreferenceManager.getDefaultSharedPreferences(context)

    private fun getString(keyId: Int, defaultValue: String): String {
        return settings.getString(context.getString(keyId), defaultValue) ?: ""
    }

    private fun getBoolean(keyId: Int, defaultValue: Boolean): Boolean {
        return settings.getBoolean(context.getString(keyId), defaultValue)
    }

    private fun getInt(keyId: Int, defaultId: Int): Int {
        return settings.getInt(context.getString(keyId), context.resources.getInteger(defaultId))
    }

    private fun getIntValue(keyId: Int, defaultValue: Int): Int {
        return settings.getInt(context.getString(keyId), defaultValue)
    }

    private fun getLong(keyId: Int, defaultValue: Long): Long {
        return settings.getLong(context.getString(keyId), defaultValue)
    }

    private fun getStringSet(keyId: Int, defaultValue: Set<String>): Set<String> {
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

    fun setEnableTime(hour: Int, minute:Int) {
        return setString(R.string.settings_key_enable_time, "$hour:$minute")
    }

    fun getEnableTime() : String {
        return getString(R.string.settings_key_enable_time, "00:00")
    }

}