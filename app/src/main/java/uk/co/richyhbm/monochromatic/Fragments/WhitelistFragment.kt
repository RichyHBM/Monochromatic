package uk.co.richyhbm.monochromatic.Fragments

import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.view.*
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.whitelist_fragment.*
import uk.co.richyhbm.monochromatic.Adapters.AppDataAdapter
import uk.co.richyhbm.monochromatic.Data.AppData
import uk.co.richyhbm.monochromatic.R
import uk.co.richyhbm.monochromatic.Utilities.AppAsyncTaskLoader
import uk.co.richyhbm.monochromatic.Utilities.Settings


class WhitelistFragment : BaseFragment(), LoaderManager.LoaderCallbacks<List<AppData>> {

    val LOADER_ID = 1234
    var showSystem = false

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<AppData>> {
        app_list_recycler_refresh_layout.isRefreshing = true
        return AppAsyncTaskLoader(requireContext())
    }

    override fun onLoaderReset(loader: Loader<List<AppData>>) {
        (app_list_recycler_view.adapter as AppDataAdapter).swap(listOf())
    }

    override fun onLoadFinished(loader: Loader<List<AppData>>, data: List<AppData>) {
        val settings = Settings(requireContext())
        settings.clearAppWhiteListUninstalled(data.map { it.packageName }.toSet())

        val sortedFilteredData =
            data.filter { appData -> showSystem || (!showSystem && appData.flags and ApplicationInfo.FLAG_SYSTEM == 0) }
                .sortedBy { appData -> appData.appName.toLowerCase() }

        (app_list_recycler_view.adapter as AppDataAdapter).swap(sortedFilteredData)
        app_list_recycler_refresh_layout.isRefreshing = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.whitelist_fragment, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this)

        app_list_recycler_refresh_layout.setOnRefreshListener {
            LoaderManager.getInstance(this).restartLoader(LOADER_ID, null, this)
        }

        app_list_recycler_view.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        app_list_recycler_view.layoutManager = layoutManager
        app_list_recycler_view.adapter = AppDataAdapter(arrayListOf())
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        showBackButton(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.whitelist_menu, menu)
        menu.findItem(R.id.whitelist_menu_show_system).isChecked = showSystem
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.whitelist_menu_show_system -> {
                item.isChecked = !item.isChecked
                showSystem = item.isChecked
                LoaderManager.getInstance(this).restartLoader(LOADER_ID, null, this)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}