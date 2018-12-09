package uk.co.richyhbm.monochromatic.Activities

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import uk.co.richyhbm.monochromatic.Fragments.MainFragment
import uk.co.richyhbm.monochromatic.Fragments.NoPermissionsDialogFragment
import uk.co.richyhbm.monochromatic.Fragments.PreferencesFragment
import uk.co.richyhbm.monochromatic.R
import uk.co.richyhbm.monochromatic.Services.MonochromeService
import uk.co.richyhbm.monochromatic.Utilities.Permissions
import uk.co.richyhbm.monochromatic.Utilities.Settings


class MainActivity : AppCompatActivity() {
    val settings by lazy { Settings(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        supportActionBar?.elevation = 0.0f

        if(!Permissions.hasSecureSettingsPermission(this)) {
            NoPermissionsDialogFragment().show(supportFragmentManager, "NoPermissionsDialog")
            settings.setEnabled(false)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, MainFragment())
            .commit()

        settings.registerPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            if (settings.isEnabled()) {
                MonochromeService.startService(this)
            } else {
                MonochromeService.stopService(this)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_menu_settings -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, PreferencesFragment())
                    .addToBackStack(null)
                    .commit()
            }
            R.id.main_menu_about -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}
