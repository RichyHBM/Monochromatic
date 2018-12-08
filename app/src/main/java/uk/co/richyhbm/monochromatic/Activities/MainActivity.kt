package uk.co.richyhbm.monochromatic.Activities

import android.content.SharedPreferences
import android.os.Bundle
import uk.co.richyhbm.monochromatic.Fragments.MainFragment
import uk.co.richyhbm.monochromatic.Fragments.NoPermissionsDialogFragment
import uk.co.richyhbm.monochromatic.R
import uk.co.richyhbm.monochromatic.Services.MonochromeService
import uk.co.richyhbm.monochromatic.Utilities.Permissions


class MainActivity : BaseActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, MainFragment())
            .commit()

        settings.registerPreferenceChangeListener(this)

        if(!Permissions.hasSecureSettingsPermission(this)) {
            NoPermissionsDialogFragment().show(supportFragmentManager, "NoPermissionsDialog")
            settings.setEnabled(false)
        }

        if(settings.isEnabled()) {
            MonochromeService.startService(this)
        }
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        if(settings.isEnabled()) {
            MonochromeService.startService(this)
        } else {
            MonochromeService.stopService(this)
        }
    }
}
