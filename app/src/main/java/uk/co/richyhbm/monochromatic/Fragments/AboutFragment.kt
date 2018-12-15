package uk.co.richyhbm.monochromatic.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import uk.co.richyhbm.monochromatic.BuildConfig
import uk.co.richyhbm.monochromatic.R

class AboutFragment : BaseFragment() {
    private class LibraryInfo(val name: String, val creator: String, val license: String)
    private val libraries = listOf(
        LibraryInfo("Kotlin", "JetBrains", "Apache 2.0"),
        LibraryInfo("Support AppCompat", "Google", "Apache 2.0"),
        LibraryInfo("Support Constraint-Layout", "Google", "Apache 2.0"),
        LibraryInfo("Support Preference-v7", "Google", "Apache 2.0")
    )


    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(false)
        showBackButton(true)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val aboutView = inflater.inflate(R.layout.about_fragment, container, false)

        aboutView.findViewById<TextView>(R.id.about_header_version).text = "Version " + BuildConfig.VERSION_NAME
        val itemsLayout = aboutView.findViewById<LinearLayout>(R.id.about_library_list)

        for (library in libraries) {
            val item = inflater.inflate(R.layout.about_list_item, itemsLayout, false)
            item.findViewById<TextView>(R.id.about_list_name).text = library.name
            item.findViewById<TextView>(R.id.about_list_creator).text = library.creator
            item.findViewById<TextView>(R.id.about_list_license).text = library.license
            itemsLayout.addView(item)
        }

        return aboutView
    }
}