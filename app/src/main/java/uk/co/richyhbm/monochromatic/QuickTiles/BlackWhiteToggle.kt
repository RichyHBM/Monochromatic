package uk.co.richyhbm.monochromatic.QuickTiles

import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast
import androidx.annotation.RequiresApi
import uk.co.richyhbm.monochromatic.R
import uk.co.richyhbm.monochromatic.Utilities.Permissions
import uk.co.richyhbm.monochromatic.Utilities.SecureSettings
import uk.co.richyhbm.monochromatic.Utilities.Settings

@RequiresApi(api = Build.VERSION_CODES.N)
class BlackWhiteToggle : TileService() {
    private val settings by lazy { Settings(applicationContext) }

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
        Toast.makeText(applicationContext, R.string.toggling_please_wait, Toast.LENGTH_SHORT).show()

        if (Permissions.hasSecureSettingsPermission(applicationContext)) {
            val cr = applicationContext.contentResolver
            SecureSettings.toggleMonochrome(!SecureSettings.isMonochromeEnabled(cr), cr)
        } else {
            Toast.makeText(applicationContext, R.string.permission_missing, Toast.LENGTH_SHORT).show()
        }

        qsTile.state = getTileState()
        qsTile.updateTile()
    }

    private fun getTileState() : Int = if(Permissions.hasSecureSettingsPermission(applicationContext)) {
        if(SecureSettings.isMonochromeEnabled(applicationContext.contentResolver)) Tile.STATE_INACTIVE else Tile.STATE_ACTIVE
    } else Tile.STATE_UNAVAILABLE

}