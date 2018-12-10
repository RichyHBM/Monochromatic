package uk.co.richyhbm.monochromatic.Activities

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.FragmentManager
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
                if(!supportFragmentManager.lastOnStackIsFragmentOf(PreferencesFragment::class.java.name)) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, PreferencesFragment())
                        .addToBackStack(PreferencesFragment::class.java.name)
                        .commit()
                }
            }
            R.id.main_menu_about -> {
                AlertDialog.Builder(this)
                    .setMessage(getString(R.string.app_info_3))
                    .setPositiveButton(android.R.string.ok, null)
                    .create()
                    .show()
            }
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

    private fun FragmentManager.lastOnStackIsFragmentOf(fragmentClassName: String) : Boolean = this.backStackEntryCount > 0
            && this.getBackStackEntryAt(this.backStackEntryCount - 1).name == fragmentClassName
}
