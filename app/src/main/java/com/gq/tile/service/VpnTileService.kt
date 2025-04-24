package com.gq.tile.service

import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.gq.tile.R

class VpnTileService : TileService() {

    override fun onTileAdded() {
        qsTile?.state = Tile.STATE_ACTIVE
        qsTile?.label = "VPN"
        qsTile?.icon = Icon.createWithResource(this, R.drawable.ic_vpn_key)
        qsTile?.updateTile()
    }

    override fun onStartListening() {
        super.onStartListening()
        qsTile.state = Tile.STATE_ACTIVE
        qsTile.label = "VPN"
        qsTile.icon = Icon.createWithResource(this, R.drawable.ic_vpn_key)
        qsTile.updateTile()
    }

    override fun onClick() {
        try {
            val intent = Intent(Settings.ACTION_VPN_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startActivityAndCollapse(pendingIntent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}