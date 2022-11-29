package com.mathroda.notifications.util

import android.app.NotificationManager
import android.content.Context

/**
 * Gets the [NotificationManager] system service.
 *
 * @return the [NotificationManager] system service
 */
fun Context.getNotificationManager() =
    getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
