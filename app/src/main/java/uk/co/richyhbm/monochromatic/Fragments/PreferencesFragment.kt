package uk.co.richyhbm.monochromatic.Fragments

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import uk.co.richyhbm.monochromatic.R


class PreferencesFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(p0: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
