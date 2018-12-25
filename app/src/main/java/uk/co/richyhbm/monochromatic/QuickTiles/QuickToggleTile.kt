package uk.co.richyhbm.monochromatic.QuickTiles

import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.support.annotation.RequiresApi
import uk.co.richyhbm.monochromatic.Utilities.SecureSettings
import uk.co.richyhbm.monochromatic.Utilities.Settings


@RequiresApi(api = Build.VERSION_CODES.N)
class QuickToggleTile : TileService() {
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

        if (settings.isEnabled()) {
            val ctxResolver = applicationContext.contentResolver
            val isMonochrome = SecureSettings.isMonochromeEnabled(ctxResolver)
            SecureSettings.toggleMonochrome(!isMonochrome, ctxResolver)
        }
        qsTile.state = getTileState()
        qsTile.updateTile()
    }

    private fun getTileState() : Int = if(settings.isEnabled()) {
        if(SecureSettings.isMonochromeEnabled(applicationContext.contentResolver)) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
    } else Tile.STATE_UNAVAILABLE

}