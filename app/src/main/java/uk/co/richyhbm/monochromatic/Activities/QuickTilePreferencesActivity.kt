package uk.co.richyhbm.monochromatic.Activities

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import uk.co.richyhbm.monochromatic.QuickTiles.DisableTempTile
import uk.co.richyhbm.monochromatic.R
import uk.co.richyhbm.monochromatic.Receivers.DisableMonochromeForSessionReceiver
import uk.co.richyhbm.monochromatic.Utilities.Permissions


class QuickTilePreferencesActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val qsTile = intent.getParcelableExtra<ComponentName>(Intent.EXTRA_COMPONENT_NAME)

            Toast.makeText(applicationContext, R.string.toggling_please_wait, Toast.LENGTH_SHORT).show()

            if (Permissions.hasSecureSettingsPermission(applicationContext)) {
                when (qsTile.className) {
                    DisableTempTile::class.java.name ->
                        DisableMonochromeForSessionReceiver.disableForSession(applicationContext)

                }
            } else {
                Toast.makeText(applicationContext, R.string.permission_missing, Toast.LENGTH_SHORT).show()

            }
        }

        finish()
    }
}