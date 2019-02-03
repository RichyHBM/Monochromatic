package uk.co.richyhbm.monochromatic.Utilities

import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import uk.co.richyhbm.monochromatic.R



object Permissions {
    fun hasSecureSettingsPermission(context: Context) : Boolean {
        return context.checkCallingOrSelfPermission(context.getString(R.string.write_secure_settings_permission)) == PackageManager.PERMISSION_GRANTED
    }

    fun hasUsageStatsPermission(context: Context) : Boolean {
        val appOps = context
            .getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(), context.packageName
        )

        return if (mode == AppOpsManager.MODE_DEFAULT) {
            context.checkCallingOrSelfPermission("android.permission.PACKAGE_USAGE_STATS") == PackageManager.PERMISSION_GRANTED
        } else {
            mode == AppOpsManager.MODE_ALLOWED
        }
    }
}