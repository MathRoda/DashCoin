package com.mathroda.notifications.coins

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.mathroda.core.util.Constants
import com.mathroda.notifications.util.getNotificationManager

class CoinsNotificationChannel(context: Context) {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = Constants.COINS_CHANNEL_NAME
            val description = Constants.COINS_CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH

            NotificationChannel(CHANNEL_ID, name, importance).apply {
                this.description = description
                context.getNotificationManager()?.createNotificationChannel(this)
            }
        }
    }

    fun getChannelId() = CHANNEL_ID

    companion object {
        const val CHANNEL_ID = "coins_notification_channel"
    }
}
