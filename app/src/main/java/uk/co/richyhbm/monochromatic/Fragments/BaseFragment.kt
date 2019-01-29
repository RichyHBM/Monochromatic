package uk.co.richyhbm.monochromatic.Fragments

import androidx.appcompat.app.AppCompatActivity

abstract class BaseFragment : androidx.fragment.app.Fragment() {

    fun showBackButton(show: Boolean) {
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(show)
    }
}