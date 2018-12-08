package uk.co.richyhbm.monochromatic.Fragments

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import uk.co.richyhbm.monochromatic.R


class MainFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) {
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences)
    }
}
