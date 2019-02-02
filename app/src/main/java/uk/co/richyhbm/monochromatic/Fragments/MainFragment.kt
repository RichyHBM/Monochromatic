package uk.co.richyhbm.monochromatic.Fragments

import android.os.Bundle
import android.view.*
import uk.co.richyhbm.monochromatic.R


class MainFragment : BaseFragment() {
    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        showBackButton(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }
}