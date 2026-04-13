package com.mohamedabdelazeim.zekr

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.mohamedabdelazeim.zekr.ui.MainActivity

class ZekrService : Service() {

    private var mediaPlayer: MediaPlayer? = null

    companion object {
        const val CHANNEL_ID = "zekr_channel"
        const val NOTIFICATION_ID = 1001
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val index = ZekrPrefs.nextIndex(this)
        val zekr = ZekrData.list[index]

        // Show notification with father's photo
        val notification = buildNotification(zekr.name)
        startForeground(NOTIFICATION_ID, notification)

        // Play audio if available
        if (zekr.audioRes != null) {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(this, zekr.audioRes)
            mediaPlayer?.setOnCompletionListener {
                it.release()
                stopSelf()
            }
            mediaPlayer?.start()
        } else {
            // No audio - just show notification for 5 seconds then stop
            android.os.Handler(mainLooper).postDelayed({ stopSelf() }, 5000)
        }

        return START_NOT_STICKY
    }

    private fun buildNotification(zekrName: String): Notification {
        val openIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        // Father's photo for notification
        val largeBitmap = BitmapFactory.decodeResource(resources, R.drawable.notification_father)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(largeBitmap)
            .setContentTitle("محمد عبد العظيم لذكر الله")
            .setContentText(zekrName)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(largeBitmap)
                    .bigLargeIcon(null as android.graphics.Bitmap?)
            )
            .setContentIntent(openIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "أذكار",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "إشعارات الأذكار الصوتية"
            enableVibration(true)
        }
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
