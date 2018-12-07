package uk.co.richyhbm.monochromatic.Utilities

import android.content.Context
import android.content.pm.PackageManager
import uk.co.richyhbm.monochromatic.R

object Permissions {
    fun hasSecureSettingsPermission(context: Context) : Boolean {
        return context.checkCallingOrSelfPermission(context.getString(R.string.write_secure_settings_permission)) == PackageManager.PERMISSION_GRANTED
    }
}