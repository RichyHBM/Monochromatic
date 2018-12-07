package uk.co.richyhbm.monochromatic.Activities

import android.os.Bundle
import android.widget.TextView
import uk.co.richyhbm.monochromatic.Constants
import uk.co.richyhbm.monochromatic.R
import uk.co.richyhbm.monochromatic.Utilities.Permissions

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (Permissions.hasSecureSettingsPermission(this)) {
            true  -> setContentView(R.layout.activity_main)
            false -> {
                setContentView(R.layout.activity_no_permissions)
                findViewById<TextView>(R.id.adb_command_text)?.text = Constants.adbCommand
            }
        }
    }
}
