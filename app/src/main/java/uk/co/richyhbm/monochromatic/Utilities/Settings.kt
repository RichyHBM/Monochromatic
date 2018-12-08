package uk.co.richyhbm.monochromatic.Utilities

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class Settings(context: Context) {
    val settings = PreferenceManager.getDefaultSharedPreferences(context)

    fun registerPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        settings.registerOnSharedPreferenceChangeListener(listener)
    }
}