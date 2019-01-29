package uk.co.richyhbm.monochromatic.QuickTiles

import android.content.Intent
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import uk.co.richyhbm.monochromatic.Receivers.DisableMonochromeForScreenReceiver
import uk.co.richyhbm.monochromatic.Utilities.Permissions
import uk.co.richyhbm.monochromatic.Utilities.Settings


@RequiresApi(api = Build.VERSION_CODES.N)
class DisableTempTile : TileService() {
    private val settings by lazy { Settings(applicationContext)}

    override fun onTileAdded() {
        super.onTileAdded()

        qsTile.state = getTileState()
        qsTile.updateTile()
    }

    override fun onStartListening() {
        super.onStartListening()
        qsTile.state = getTileState()
        qsTile.updateTile()
    }

    override fun onStopListening() {
        super.onStopListening()
        qsTile.state = getTileState()
        qsTile.updateTile()
    }

    override fun onClick() {
        super.onClick()

        if (Permissions.hasSecureSettingsPermission(applicationContext)) {
            sendBroadcast(Intent(applicationContext, DisableMonochromeForScreenReceiver::class.java))
        }

        qsTile.state = getTileState()
        qsTile.updateTile()
    }

    private fun getTileState() : Int = if(Permissions.hasSecureSettingsPermission(applicationContext)) {
        if(settings.isEnabled() && settings.isAllowed()) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
    } else Tile.STATE_UNAVAILABLE

}