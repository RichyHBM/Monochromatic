package uk.co.richyhbm.monochromatic.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import uk.co.richyhbm.monochromatic.Data.AppData
import uk.co.richyhbm.monochromatic.R
import uk.co.richyhbm.monochromatic.Utilities.AppAsyncTaskLoader


class WhitelistFragment: BaseFragment(), LoaderManager.LoaderCallbacks<List<AppData>> {

    val LOADER_ID = 1234

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<AppData>> {
        return AppAsyncTaskLoader(requireContext())
    }

    override fun onLoaderReset(loader: Loader<List<AppData>>) {
        //loader.forceLoad()
    }

    override fun onLoadFinished(loader: Loader<List<AppData>>, data: List<AppData>) {
        for(appData in data) {
            Log.d("APP", appData.appName)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.whitelist_fragment, container, false)

        return fragmentView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this)
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(false)
        showBackButton(true)
    }
}