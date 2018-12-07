package uk.co.richyhbm.monochromatic

import android.content.Context

object Constants {
    const val displayDaltonizerEnabled = "accessibility_display_daltonizer_enabled"
    const val displayDaltonizer = "accessibility_display_daltonizer"

    const val daltonizerDisabled = -1
    const val daltonizerSimulateMonochrome = 0

    fun getAdbCommand(context: Context) : String = context.getString(R.string.adb_command, BuildConfig.APPLICATION_ID)
}