package uk.co.richyhbm.monochromatic.Fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import uk.co.richyhbm.monochromatic.R
import uk.co.richyhbm.monochromatic.Utilities.Settings
import java.util.concurrent.TimeUnit


class PreferencesFragment : PreferenceFragmentCompat() {
    val settings: Settings by lazy { Settings(requireContext()) }

    override fun onCreatePreferences(p0: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val minutes = settings.getEnableTime() % 60
        val hours = TimeUnit.MINUTES.toHours(settings.getEnableTime().toLong())
        findPreference(getString(R.string.settings_key_enable_time)).summary = String.format("%02d:%02d", hours, minutes)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean =
        when (preference.key) {
            getString(R.string.settings_key_enable_time) -> {
                showTimePickerDialog {
                    settings.setEnableTime(it)
                    val minutes = settings.getEnableTime() % 60
                    val hours = TimeUnit.MINUTES.toHours(settings.getEnableTime().toLong())
                    findPreference(getString(R.string.settings_key_enable_time)).summary = String.format("%02d:%02d", hours, minutes)
                }
            }
            else -> super.onPreferenceTreeClick(preference)
        }

    private fun showTimePickerDialog(callback: (Int) -> Unit ): Boolean {
        TimePickerDialog(requireContext(),
            { _, hour, minute ->
                val newValue = hour * 60 + minute
                callback(newValue)
            },
            TimeUnit.MINUTES.toHours(settings.getEnableTime().toLong()).toInt(),
            settings.getEnableTime() % 60,
            true).show()
        return true
    }
}
