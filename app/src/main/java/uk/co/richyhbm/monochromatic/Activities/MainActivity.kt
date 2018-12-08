package uk.co.richyhbm.monochromatic.Activities

import android.os.Bundle
import uk.co.richyhbm.monochromatic.Fragments.MainFragment
import uk.co.richyhbm.monochromatic.Fragments.NoPermissionsFragment
import uk.co.richyhbm.monochromatic.R
import uk.co.richyhbm.monochromatic.Utilities.Permissions


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            val fragment = when(Permissions.hasSecureSettingsPermission(this)) {
                true -> MainFragment.newInstance()
                false -> NoPermissionsFragment.newInstance()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()
        }
    }
}
