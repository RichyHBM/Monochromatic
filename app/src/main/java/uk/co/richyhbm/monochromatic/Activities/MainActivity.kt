package uk.co.richyhbm.monochromatic.Activities

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SwitchCompat
import android.view.MenuItem
import uk.co.richyhbm.monochromatic.Fragments.AboutFragment
import uk.co.richyhbm.monochromatic.Fragments.MainFragment
import uk.co.richyhbm.monochromatic.Fragments.NoPermissionsDialogFragment
import uk.co.richyhbm.monochromatic.Fragments.PreferencesFragment
import uk.co.richyhbm.monochromatic.R
import uk.co.richyhbm.monochromatic.Services.MonochromeService
import uk.co.richyhbm.monochromatic.Utilities.Permissions
import uk.co.richyhbm.monochromatic.Utilities.SecureSettings
import uk.co.richyhbm.monochromatic.Utilities.Settings


class MainActivity : AppCompatActivity() {
    val settings by lazy { Settings(this) }

    private val preferencesChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
        if (settings.isEnabled()) {
            MonochromeService.startService(this)
            SecureSettings.toggleFilters(settings.isAllowed(), contentResolver, settings)
        } else {
            MonochromeService.stopService(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        supportActionBar?.elevation = 0.0f

        if(!Permissions.hasSecureSettingsPermission(this)) {
            NoPermissionsDialogFragment().show(supportFragmentManager, "NoPermissionsDialog")
            settings.setEnabled(false)
        } else {
            if (settings.isEnabled()) {
                MonochromeService.startService(this)
                SecureSettings.toggleFilters(settings.isAllowed(), contentResolver, settings)
            } else {
                MonochromeService.stopService(this)
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, MainFragment())
            .commit()

        settings.registerPreferenceChangeListener(preferencesChangeListener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.main_menu_force_color -> {
            SecureSettings.resetAllFilters(contentResolver, settings)
            settings.setEnabled(false)
            MonochromeService.stopService(this)
            findViewById<SwitchCompat>(R.id.enabled_switch)?.isChecked = false
            true
        }
        R.id.main_menu_settings -> {
            if (!supportFragmentManager.lastOnStackIsFragmentOf(PreferencesFragment::class.java.name)) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, PreferencesFragment())
                    .addToBackStack(PreferencesFragment::class.java.name)
                    .commit()
            }
            true
        }
        R.id.main_menu_about -> {
            if (!supportFragmentManager.lastOnStackIsFragmentOf(AboutFragment::class.java.name)) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, AboutFragment())
                    .addToBackStack(AboutFragment::class.java.name)
                    .commit()
            }
            true
        }
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
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
