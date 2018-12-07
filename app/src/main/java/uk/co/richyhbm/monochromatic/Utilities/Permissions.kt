package uk.co.richyhbm.monochromatic.Utilities

import android.content.Context
import android.content.pm.PackageManager
import uk.co.richyhbm.monochromatic.Constants

object Permissions {
    fun hasSecureSettingsPermission(context: Context) : Boolean {
        return context.checkCallingOrSelfPermission(Constants.writeSecureSettingsPermission) == PackageManager.PERMISSION_GRANTED
    }
}