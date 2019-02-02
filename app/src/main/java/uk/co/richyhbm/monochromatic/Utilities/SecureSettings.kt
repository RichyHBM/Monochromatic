package uk.co.richyhbm.monochromatic.Utilities

import android.content.ContentResolver
import android.provider.Settings


object SecureSettings {
    fun toggleFilters(
        enabled: Boolean,
        contentResolver: ContentResolver,
        settings: uk.co.richyhbm.monochromatic.Utilities.Settings
    ) {
        if (settings.isFilterBluelightEnabled()) {
            toggleMonochromeAndBluelightFilter(enabled, contentResolver)
        } else {
            toggleMonochrome(enabled, contentResolver)
        }
    }

    fun toggleMonochrome(enabled: Boolean, contentResolver: ContentResolver) {
        Settings.Secure.putInt(contentResolver, Constants.secureSetting_displayDaltonizerEnabled, enabled.int)
        if (enabled) {
            Settings.Secure.putInt(
                contentResolver,
                Constants.secureSetting_displayDaltonizer,
                Constants.secureSettingValue_daltonizerSimulateMonochrome
            )
        } else {
            Settings.Secure.putInt(
                contentResolver,
                Constants.secureSetting_displayDaltonizer,
                Constants.secureSettingValue_daltonizerDisabled
            )
        }
    }

    fun resetAllFilters(contentResolver: ContentResolver, settings: uk.co.richyhbm.monochromatic.Utilities.Settings) {
        if (settings.isFilterBluelightEnabled()) {
            resetMonochromeAndBluelightFilter(contentResolver)
        } else {
            resetMonochrome(contentResolver)
        }
    }

    fun resetMonochrome(contentResolver: ContentResolver) {
        Settings.Secure.putInt(contentResolver, Constants.secureSetting_displayDaltonizerEnabled, false.int)
        Settings.Secure.putInt(
            contentResolver,
            Constants.secureSetting_displayDaltonizer,
            Constants.secureSettingValue_daltonizerDisabled
        )
    }

    fun isMonochromeEnabled(contentResolver: ContentResolver): Boolean {
        return Settings.Secure.getInt(
            contentResolver,
            Constants.secureSetting_displayDaltonizerEnabled,
            false.int
        ) == true.int &&
                Settings.Secure.getInt(
                    contentResolver,
                    Constants.secureSetting_displayDaltonizer,
                    Constants.secureSettingValue_daltonizerDisabled
                ) == Constants.secureSettingValue_daltonizerSimulateMonochrome
    }

    fun toggleBluelightFilter(enabled: Boolean, contentResolver: ContentResolver) {
        Settings.Secure.putInt(contentResolver, Constants.secureSetting_displayBluelightFilterEnabled, enabled.int)
    }

    fun isBluelightFilterEnabled(contentResolver: ContentResolver) =
        Settings.Secure.getInt(
            contentResolver,
            Constants.secureSetting_displayBluelightFilterEnabled,
            false.int
        ) == true.int

    fun setBlueFilterTemperature(temperature: Int, contentResolver: ContentResolver) {
        Settings.Secure.putInt(contentResolver, Constants.secureSetting_displayBluelightFilterTemperature, temperature)
    }

    fun resetBlueFilterTemperature(contentResolver: ContentResolver) {
        Settings.Secure.putInt(
            contentResolver,
            Constants.secureSetting_displayBluelightFilterTemperature,
            Constants.defaultBluelightFilterTemperature
        )
    }

    fun toggleMonochromeAndBluelightFilter(enabled: Boolean, contentResolver: ContentResolver) {
        toggleMonochrome(enabled, contentResolver)
        toggleBluelightFilter(true, contentResolver)
    }

    fun isMonochromeAndBluelightFilterEnabled(contentResolver: ContentResolver): Boolean {
        return isBluelightFilterEnabled(contentResolver) && isMonochromeEnabled(contentResolver)
    }

    fun resetMonochromeAndBluelightFilter(contentResolver: ContentResolver) {
        resetMonochrome(contentResolver)
        toggleBluelightFilter(false, contentResolver)
    }

    val Boolean.int get() = if (this) 1 else 0
}