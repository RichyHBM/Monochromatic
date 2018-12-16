package uk.co.richyhbm.monochromatic.Fragments

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.widget.NumberPicker
import uk.co.richyhbm.monochromatic.R
import uk.co.richyhbm.monochromatic.Utilities.Settings
import java.util.concurrent.TimeUnit


class PreferencesFragment : PreferenceFragmentCompat() {
    val settings: Settings by lazy { Settings(requireContext()) }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(false)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreatePreferences(p0: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val enableTime = findPreference(getString(R.string.settings_key_enable_time))
        val disableTime = findPreference(getString(R.string.settings_key_disable_time))
        val lowBatteryAmount = findPreference(getString(R.string.settings_key_enable_with_low_battery_amount))

        findPreference(getString(R.string.settings_key_enable_with_time)).setOnPreferenceChangeListener { _, newValue ->
            newValue as Boolean
            enableTime.isVisible = newValue
            disableTime.isVisible = newValue
            true
        }

        findPreference(getString(R.string.settings_key_enable_with_low_battery)).setOnPreferenceChangeListener { _, newValue ->
            newValue as Boolean
            lowBatteryAmount.isVisible = newValue
            true
        }

        enableTime.isVisible = settings.shouldEnableAtTime()
        disableTime.isVisible = settings.shouldEnableAtTime()
        lowBatteryAmount.isVisible = settings.shouldEnableAtLowBattery()

        enableTime.summary = getTimeToString(settings.getEnableTime())
        disableTime.summary = getTimeToString(settings.getDisableTime())
        lowBatteryAmount.summary = settings.getLowBatteryLevel().toString() + "%"
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean =
        when (preference.key) {
            getString(R.string.settings_key_enable_time) -> {
                showTimePickerDialog {
                    settings.setEnableTime(it)
                    findPreference(getString(R.string.settings_key_enable_time)).summary = getTimeToString(it)
                }
            }
            getString(R.string.settings_key_disable_time) -> {
                showTimePickerDialog {
                    settings.setDisableTime(it)
                    findPreference(getString(R.string.settings_key_disable_time)).summary = getTimeToString(it)
                }
            }
            getString(R.string.settings_key_enable_with_low_battery_amount) -> {
                showNumberPickerDialog {
                    settings.setLowBatteryLevel(it)
                    findPreference(getString(R.string.settings_key_enable_with_low_battery_amount)).summary = it.toString() + "%"
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

    private fun showNumberPickerDialog(callback: (Int) -> Unit ): Boolean {
        val numberPicker = NumberPicker(context)
        numberPicker.minValue = 0
        numberPicker.maxValue = 100
        numberPicker.wrapSelectorWheel = false
        numberPicker.setOnValueChangedListener { _, _, newVal -> callback(newVal) }
        AlertDialog.Builder(context).setView(numberPicker).setPositiveButton(android.R.string.ok, null).show()
        return true
    }

    private fun getTimeToString(time: Int) : String {
        val minutes = time % 60
        val hours = TimeUnit.MINUTES.toHours(time.toLong())
        return String.format("%02d:%02d", hours, minutes)
    }
}
