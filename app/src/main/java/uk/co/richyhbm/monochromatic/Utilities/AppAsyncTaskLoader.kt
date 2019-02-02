package uk.co.richyhbm.monochromatic.Utilities

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.loader.content.AsyncTaskLoader
import uk.co.richyhbm.monochromatic.Data.AppData

class AppAsyncTaskLoader(context: Context): AsyncTaskLoader<List<AppData>>(context){
    override fun loadInBackground(): List<AppData> {
        val packageManager = context.packageManager ?: return listOf()
        val appInfoList = packageManager.getInstalledApplications(0) ?: return listOf()
        return appInfoList.map {appInfoToAppData(packageManager, it)}
    }

    private fun appInfoToAppData(packageManager: PackageManager, appInfo: ApplicationInfo): AppData {
        return AppData(
            appInfo.packageName,
            appInfo.loadLabel(packageManager).toString(),
            appInfo.loadIcon(packageManager)
        )
    }

    override fun onStartLoading() {
        forceLoad()
    }
}