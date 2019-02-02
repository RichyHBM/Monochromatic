package uk.co.richyhbm.monochromatic.Fragments

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val enableTime = findPreference<Preference>(getString(R.string.settings_key_enable_time))
        val disableTime = findPreference<Preference>(getString(R.string.settings_key_disable_time))
        val lowBatteryAmount =
            findPreference<Preference>(getString(R.string.settings_key_enable_with_low_battery_amount))
        val bluelightFilterTemperature =
            findPreference<Preference>(getString(R.string.settings_key_bluelight_filter_temperature))

        findPreference<Preference>(getString(R.string.settings_key_enable_with_time)).setOnPreferenceChangeListener { _, newValue ->
            newValue as Boolean
            enableTime.isVisible = newValue
            disableTime.isVisible = newValue
            true
        }

        findPreference<Preference>(getString(R.string.settings_key_enable_with_low_battery)).setOnPreferenceChangeListener { _, newValue ->
            newValue as Boolean
            lowBatteryAmount.isVisible = newValue
            true
        }

        val deviceHasSystemBluelightFilter = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O
        val blueFilterSwitch = findPreference<Preference>(getString(R.string.settings_key_bluelight_filter_enabled))
        blueFilterSwitch.isVisible = deviceHasSystemBluelightFilter
        blueFilterSwitch.setOnPreferenceChangeListener { _, newValue ->
            newValue as Boolean
            bluelightFilterTemperature.isVisible = newValue
            true
        }

        enableTime.isVisible = settings.shouldEnableAtTime()
        disableTime.isVisible = settings.shouldEnableAtTime()
        lowBatteryAmount.isVisible = settings.shouldEnableAtLowBattery()
        bluelightFilterTemperature.isVisible = settings.isFilterBluelightEnabled()

        enableTime.summary = getTimeToString(settings.getEnableTime())
        disableTime.summary = getTimeToString(settings.getDisableTime())
        lowBatteryAmount.summary = settings.getLowBatteryLevel().toString() + "%"
        bluelightFilterTemperature.summary = settings.getBluelightFilterTemperature().toString()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean =
        when (preference.key) {
            getString(R.string.settings_key_enable_time) -> {
                showTimePickerDialog(settings.getEnableTime()) {
                    settings.setEnableTime(it)
                    findPreference<Preference>(getString(R.string.settings_key_enable_time)).summary =
                            getTimeToString(it)
                }
            }
            getString(R.string.settings_key_disable_time) -> {
                showTimePickerDialog(settings.getDisableTime()) {
                    settings.setDisableTime(it)
                    findPreference<Preference>(getString(R.string.settings_key_disable_time)).summary =
                            getTimeToString(it)
                }
            }
            getString(R.string.settings_key_enable_with_low_battery_amount) -> {
                showNumberPickerDialog(settings.getLowBatteryLevel()) {
                    settings.setLowBatteryLevel(it)
                    findPreference<Preference>(getString(R.string.settings_key_enable_with_low_battery_amount)).summary =
                            it.toString() + "%"
                }
            }
            getString(R.string.settings_key_bluelight_filter_temperature) -> {
                showTemperatureSeekBarDialog {
                    settings.setBluelightFilterTemperature(it)
                    findPreference<Preference>(getString(R.string.settings_key_bluelight_filter_temperature)).summary =
                            settings.getBluelightFilterTemperature().toString()
                }
            }
            else -> super.onPreferenceTreeClick(preference)
        }

    private fun showTimePickerDialog(currentTime: Int, callback: (Int) -> Unit): Boolean {
        TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                val newValue = hour * 60 + minute
                callback(newValue)
            },
            TimeUnit.MINUTES.toHours(currentTime.toLong()).toInt(),
            currentTime % 60,
            true
        ).show()
        return true
    }

    private fun showTemperatureSeekBarDialog(callback: (Int) -> Unit): Boolean {
        val currentVal = settings.getBluelightFilterTemperature()
        return showSeekBarDialog(currentVal, callback)
    }

    private fun showSeekBarDialog(currentProgress: Int, callback: (Int) -> Unit): Boolean {
        val minTemp = 100
        val maxTemp = 6000
        val numSteps = 100
        val stepValue = (maxTemp - minTemp) / numSteps
        fun progressToTemperature(progress: Int) = minTemp + (progress * stepValue)
        fun temperatureToProgress(temperature: Int) = (temperature - minTemp) / stepValue

        val seekbar = SeekBar(context)

        seekbar.progress = temperatureToProgress(currentProgress)
        seekbar.max = numSteps
        seekbar.setPadding(50, 50, 50, 0)
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

            override fun onProgressChanged(p0: SeekBar?, newVal: Int, p2: Boolean) {
                val newTemp = progressToTemperature(newVal)
                callback(newTemp)
            }
        })
        AlertDialog.Builder(context).setView(seekbar).setPositiveButton(android.R.string.ok, null).show()
        return true
    }

    private fun showNumberPickerDialog(currentVal: Int, callback: (Int) -> Unit): Boolean {
        val numberPicker = NumberPicker(context)
        numberPicker.minValue = 0
        numberPicker.maxValue = 100
        numberPicker.value = currentVal
        numberPicker.wrapSelectorWheel = false
        numberPicker.setOnValueChangedListener { _, _, newVal -> callback(newVal) }
        AlertDialog.Builder(context).setView(numberPicker).setPositiveButton(android.R.string.ok, null).show()
        return true
    }

    private fun getTimeToString(time: Int): String {
        val minutes = time % 60
        val hours = TimeUnit.MINUTES.toHours(time.toLong())
        return String.format("%02d:%02d", hours, minutes)
    }
}
