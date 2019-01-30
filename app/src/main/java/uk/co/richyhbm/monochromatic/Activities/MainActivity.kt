package uk.co.richyhbm.monochromatic.Activities

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import android.view.MenuItem
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_fragment.*
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

    private val preferencesChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        when(key) {
            getString(R.string.settings_key_disable_screen) -> {}
            getString(R.string.settings_key_disable_session) -> {}
            else -> {
                if (settings.isEnabled()) {
                    MonochromeService.startService(this)
                    SecureSettings.toggleFilters(settings.isAllowed(), contentResolver, settings)
                } else {
                    MonochromeService.stopService(this)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)
        setSupportActionBar(main_bottom_bar)

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

            enable_toggle.backgroundTintList = ContextCompat.getColorStateList(this,
                if(settings.isEnabled()) android.R.color.darker_gray else android.R.color.holo_orange_dark
            )
            enable_toggle.setImageResource(if(settings.isEnabled()) R.drawable.ic_close_black_24dp else android.R.drawable.ic_lock_power_off)

            enable_toggle.setOnClickListener {
                val b = !settings.isEnabled()
                settings.setEnabled(b)

                enable_toggle.backgroundTintList = ContextCompat.getColorStateList(this,
                    if(settings.isEnabled()) android.R.color.darker_gray else android.R.color.holo_orange_dark
                )
                enable_toggle.setImageResource(if(settings.isEnabled()) R.drawable.ic_close_black_24dp else android.R.drawable.ic_lock_power_off)

                if (settings.isEnabled()) {
                    MonochromeService.startService(this)
                    if(!settings.seenNotificationDialog() &&
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        AlertDialog.Builder(this)
                            .setMessage(getString(R.string.notification_dismiss_notice))
                            .setPositiveButton(android.R.string.ok) { _, _ ->
                                settings.setSeenNotificationDialog()
                            }
                            .create()
                            .show()
                    }
                }
                else MonochromeService.stopService(this)
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

    private fun androidx.fragment.app.FragmentManager.lastOnStackIsFragmentOf(fragmentClassName: String) : Boolean = this.backStackEntryCount > 0
            && this.getBackStackEntryAt(this.backStackEntryCount - 1).name == fragmentClassName
}
