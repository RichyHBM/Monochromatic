package uk.co.richyhbm.monochromatic.Utilities

object Constants {
    /** Daltonizer */
    const val secureSetting_displayDaltonizerEnabled = "accessibility_display_daltonizer_enabled"
    const val secureSetting_displayDaltonizer = "accessibility_display_daltonizer"

    const val secureSettingValue_daltonizerDisabled = -1
    const val secureSettingValue_daltonizerSimulateMonochrome = 0

    /**
     * Night display (bluelight filter)
     * AOSP reference:
     * Oreo - http://androidxref.com/8.0.0_r4/xref/frameworks/base/core/java/com/android/internal/app/NightDisplayController.java
     * Pie - http://androidxref.com/9.0.0_r3/xref/frameworks/base/core/java/com/android/internal/app/ColorDisplayController.java
     */
    const val secureSetting_displayBluelightFilterEnabled = "night_display_activated"
    const val secureSetting_displayBluelightFilterTemperature = "night_display_color_temperature"

    const val defaultBluelightFilterTemperature = 1600
}