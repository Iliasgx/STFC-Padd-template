package com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.WorkManager
import com.iliasg.startrekfleetcommand.stfcpadd.R
import java.util.*

object AsyncNotification {

    const val NOTIFICATION_ID = 7531

    private const val CHANNEL_ID = "1357"
    private const val CHANNEL_NAME = "sync_channel"

    fun notification(appCtx: Context, id: UUID): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannelId(appCtx)

        val intent = WorkManager.getInstance(appCtx).createCancelPendingIntent(id)

        return NotificationCompat.Builder(appCtx, CHANNEL_ID)
            .setContentTitle(appCtx.getString(R.string.notification_title))
            .setContentText(appCtx.getString(R.string.notification_text))
            .setSmallIcon(R.drawable.ic_download)
            .addAction(
                NotificationCompat.Action(
                    null,
                    appCtx.getString(android.R.string.cancel),
                    intent
                )
            )
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannelId(ctx: Context) {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE)
        val service = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
    }
}