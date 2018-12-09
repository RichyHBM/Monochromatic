package uk.co.richyhbm.monochromatic.QuickTiles

import android.content.Intent
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.support.annotation.RequiresApi
import uk.co.richyhbm.monochromatic.Receivers.DisableMonochromeReceiver
import uk.co.richyhbm.monochromatic.Utilities.Settings


@RequiresApi(api = Build.VERSION_CODES.N)
class QuickDisableTile : TileService() {
    private val settings by lazy { Settings(applicationContext)}

    override fun onTileAdded() {
        super.onTileAdded()

        qsTile.state = if(settings.isEnabled()) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        qsTile.updateTile()
    }

    override fun onStartListening() {
        qsTile.state = if(settings.isEnabled()) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        qsTile.updateTile()
    }

    override fun onClick() {
        super.onClick()

        if (settings.isEnabled()) {
            val disableIntent = Intent(applicationContext, DisableMonochromeReceiver::class.java)
            sendBroadcast(disableIntent)

            qsTile.state = Tile.STATE_INACTIVE
            qsTile.updateTile()
        }
    }
}