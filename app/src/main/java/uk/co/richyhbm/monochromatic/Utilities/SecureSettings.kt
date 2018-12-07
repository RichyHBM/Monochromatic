package uk.co.richyhbm.monochromatic.Utilities

import android.content.ContentResolver
import android.provider.Settings
import uk.co.richyhbm.monochromatic.Constants


object SecureSettings {
    fun toggleMonochrome(enabled: Boolean, contentResolver: ContentResolver) {
        Settings.Secure.putInt(contentResolver, Constants.displayDaltonizerEnabled, enabled.int)
        if (enabled) {
            Settings.Secure.putInt(contentResolver, Constants.displayDaltonizer, Constants.daltonizerSimulateMonochrome)
        } else {
            Settings.Secure.putInt(contentResolver, Constants.displayDaltonizerEnabled, Constants.daltonizerDisabled)
        }
    }

    fun resetMonochrome(contentResolver: ContentResolver) {
        Settings.Secure.putInt(contentResolver, Constants.displayDaltonizerEnabled, false.int)
        Settings.Secure.putInt(contentResolver, Constants.displayDaltonizerEnabled, Constants.daltonizerDisabled)
    }

    fun isMonochromeEnabled(contentResolver: ContentResolver) : Boolean {
        return Settings.Secure.getInt(contentResolver, Constants.displayDaltonizerEnabled, false.int) == true.int &&
            Settings.Secure.getInt(contentResolver, Constants.displayDaltonizerEnabled, Constants.daltonizerDisabled) == Constants.daltonizerSimulateMonochrome
    }

    val Boolean.int get() = if (this) 1 else 0
}