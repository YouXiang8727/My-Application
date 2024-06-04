package com.youxiang8727.myapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder

class MediaProjectionService : Service() {

    private val CHANNEL = "TH-TransferHelper"

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        setNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MediaProjectionUtils.init(
            this,
            MainActivity.capturePermissionResult!!.resultCode,
            MainActivity.capturePermissionResult!!.data!!
        )
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaProjectionUtils.getInstance().onDestroy()
        stopSelf()
    }

    private fun setNotification() {
        val channel =
            NotificationChannel(CHANNEL, CHANNEL, NotificationManager.IMPORTANCE_HIGH)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val notification: Notification =
            Notification.Builder(applicationContext, CHANNEL).build()
        if (Build.VERSION.SDK_INT < 29) {
            startForeground(1, notification)
        } else {
            startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION)
        }
    }
}