package com.gq.tile.service

import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Icon
import android.media.AudioManager
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.gq.tile.R

class VolumeTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        qsTile.state = Tile.STATE_ACTIVE
        qsTile.label = "音量"
        qsTile.icon = Icon.createWithResource(this, R.drawable.text_to_speech_24dp)
        qsTile.updateTile()
    }

    override fun onClick() {
        super.onClick()
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        // 增大音量（或减小），系统会弹出音量条
        audioManager.adjustStreamVolume(
            AudioManager.STREAM_MUSIC,
            AudioManager.ADJUST_LOWER,  // 或 ADJUST_LOWER
            AudioManager.FLAG_SHOW_UI   // 显示系统音量 UI
        )
        collapseStatusBar()
    }


    private fun collapseStatusBar() {
        try {
            val intent = Intent()
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