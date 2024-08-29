package com.mathroda.notifications.sync

import com.mmk.kmpnotifier.notification.Notifier
import com.mmk.kmpnotifier.notification.NotifierManager

class SyncNotification (
    private val notifier: Notifier
) {
    fun show(
        title: String,
        description: String
    ) {
        notifier.notify(title, description)
    }

}
