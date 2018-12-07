package uk.co.richyhbm.monochromatic.Activities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import uk.co.richyhbm.monochromatic.Constants
import uk.co.richyhbm.monochromatic.R
import uk.co.richyhbm.monochromatic.Utilities.Permissions



class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (Permissions.hasSecureSettingsPermission(this)) {
            true -> setContentView(R.layout.activity_main)
            false -> noPermissionsActivity()
        }
    }

    fun noPermissionsActivity() {
        setContentView(R.layout.activity_no_permissions)
        val adbCommandText = findViewById<TextView>(R.id.adb_command_text)
        adbCommandText.text = Constants.getAdbCommand(this)
        adbCommandText.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("adb_command", Constants.getAdbCommand(this))
            clipboard.primaryClip = clip
            Toast.makeText(applicationContext, "Command copied", Toast.LENGTH_SHORT).show()
        }
    }
}
