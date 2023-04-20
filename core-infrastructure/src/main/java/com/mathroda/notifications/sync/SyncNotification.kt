package com.mathroda.notifications.sync

import android.app.Application
import androidx.core.app.NotificationCompat
import com.mathroda.notifications.util.getNotificationManager
import com.mathroda.workmanger.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SyncNotification @Inject constructor(
    @ApplicationContext private val context: Application,
    private val channel: SyncNotificationChannel
) {

    fun show(
        id: Int,
        title: String,
        description: String
    ) {
        val builder = buildNotification(
            title, description
        )

        context.getNotificationManager()?.notify(id, builder.build())
    }

    private fun buildNotification(
        title: String,
        description: String
    ) =
        NotificationCompat.Builder(context, channel.getChannelId()).apply {
            setSmallIcon(R.mipmap.ic_dashcoin)
            setContentTitle(title)
            setContentText(description)
            setAutoCancel(true)
        }
}
