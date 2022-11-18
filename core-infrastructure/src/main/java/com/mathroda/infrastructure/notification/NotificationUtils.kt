package com.mathroda.infrastructure.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mathroda.core.util.Constants.CHANNEL_ID
import com.mathroda.core.util.Constants.CHANNEL_NAME
import com.mathroda.infrastructure.R

object NotificationUtils {

    fun showNotification(context: Context, title: String, description: String, id: Int) {
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(notificationManager)

        val builder = createNotificationCompat(context, title, description)

        notificationManager.notify(id, builder.build())
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotificationCompat(
        context: Context,
        title: String,
        description: String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(R.mipmap.ic_dashcoin)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    }
}