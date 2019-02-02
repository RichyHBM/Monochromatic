package uk.co.richyhbm.monochromatic.Fragments

class WhitelistFragment: BaseFragment() {
    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(false)
        showBackButton(true)
    }
}