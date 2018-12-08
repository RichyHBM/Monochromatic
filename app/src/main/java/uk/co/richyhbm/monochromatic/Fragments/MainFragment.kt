package uk.co.richyhbm.monochromatic.Fragments

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import uk.co.richyhbm.monochromatic.R
import uk.co.richyhbm.monochromatic.Utilities.Permissions


class MainFragment : PreferenceFragmentCompat() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val enabledPreference = findPreference(getString(R.string.settings_key_monochromatic_enabled))
        enabledPreference.isEnabled = Permissions.hasSecureSettingsPermission(context!!)
    }

    override fun onCreatePreferences(p0: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
