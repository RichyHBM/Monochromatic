package uk.co.richyhbm.monochromatic.Activities

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import uk.co.richyhbm.monochromatic.QuickTiles.DisableTempTile
import uk.co.richyhbm.monochromatic.Receivers.DisableMonochromeForSessionReceiver
import uk.co.richyhbm.monochromatic.Utilities.Permissions



class QuickTilePreferencesActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val qsTile = intent.getParcelableExtra<ComponentName>(Intent.EXTRA_COMPONENT_NAME)

            if (Permissions.hasSecureSettingsPermission(applicationContext)) {
                when (qsTile.className) {
                    DisableTempTile::class.java.name -> sendBroadcast(Intent(applicationContext, DisableMonochromeForSessionReceiver::class.java))
                }
            }
        }

        finish()
    }
}