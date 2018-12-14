package uk.co.richyhbm.monochromatic.Fragments

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

abstract class BaseFragment : Fragment() {

    fun showBackButton(show: Boolean) {
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(show)
    }
}