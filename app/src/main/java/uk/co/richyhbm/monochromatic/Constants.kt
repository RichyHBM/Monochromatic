package uk.co.richyhbm.monochromatic

object Constants {
    const val writeSecureSettingsPermission = "android.permission.WRITE_SECURE_SETTINGS"
    const val adbCommand = "adb shell pm grant " + BuildConfig.APPLICATION_ID + " " + writeSecureSettingsPermission

    const val displayDaltonizerEnabled = "accessibility_display_daltonizer_enabled"
    const val displayDaltonizer = "accessibility_display_daltonizer"


    const val daltonizerDisabled = -1
    const val daltonizerSimulateMonochrome = 0
}