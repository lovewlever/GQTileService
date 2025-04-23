package com.gq.tile.service

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import androidx.core.net.toUri
import com.gq.tile.R

class GoogleTVTileService : TileService() {

    private val GoogleTVPackageName = "com.google.android.videos"
    private val GoogleTVStartActivityName = "com.google.android.videos.GoogleTvEntryPoint"
    private val GoogleTVVirtualRemoteAction = "com.google.android.apps.googletv.ACTION_VIRTUAL_REMOTE"


    override fun onTileAdded() {
        super.onTileAdded()
        qsTile?.apply {
            label = "Google TV"
            icon = Icon.createWithResource(this@GoogleTVTileService, R.drawable.ic_tv) // 你自己的图标
            state = Tile.STATE_ACTIVE
            updateTile()
        }
    }

    override fun onClick() {
        super.onClick()
        if (isAppInstalledAndEnabled()) {
            startRemoteWatchActivity()
        } else {
            startGoogleTVSystemSettingPage()
        }
    }

    private fun startGoogleTvEntryPoint() {
        try {
            val intent = Intent(Intent.ACTION_MAIN).apply {
                component = ComponentName(
                    GoogleTVPackageName,
                    GoogleTVStartActivityName
                )
                addCategory(Intent.CATEGORY_LAUNCHER)
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
            Log.d("GoogleTVTileService", "Google TV 未安装")
            startGoogleTVSystemSettingPage()
        }
    }

    private fun startGoogleTVSystemSettingPage() {
        try {
            val intent =
                Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = "package:${GoogleTVPackageName}".toUri()
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
            Log.d("GoogleTVTileService", "Google TV 未安装")
        }
    }

    private fun startRemoteWatchActivity() {
        val intent = Intent(GoogleTVVirtualRemoteAction).apply {
            addCategory(Intent.CATEGORY_DEFAULT)  // 添加默认 Category
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)  // 必须设置，确保活动在新的任务中启动
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // 使用 startActivityAndCollapse 启动并折叠通知栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startActivityAndCollapse(pendingIntent)
        }
    }

    fun isAppInstalledAndEnabled(): Boolean {
        return try {
            val pm = packageManager
            val appInfo = pm.getApplicationInfo(GoogleTVPackageName, 0)
            appInfo.enabled
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}