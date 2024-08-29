package com.mathroda.notifications.coins

import com.mathroda.core.util.Constants
import com.mmk.kmpnotifier.notification.Notifier

class CoinsNotification (
    private val notifier: Notifier
) {

    fun show(
        title: String,
        description: String
    ) {
      notifier.notify(title, description)
    }
}

fun CoinsNotification.showPositive() {
    show(
        title = Constants.TITLE,
        description = Constants.DESCRIPTION_POSITIVE,
    )
}

fun CoinsNotification.showNegative() {
    show(
        title = Constants.TITLE,
        description = Constants.DESCRIPTION_NEGATIVE
    )
}