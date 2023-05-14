package com.mathroda.notifications.coins

import android.app.Application
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.mathroda.common.navigation.DestinationsDeepLink
import com.mathroda.core.state.UserState
import com.mathroda.core.util.Constants
import com.mathroda.infrastructure.R
import com.mathroda.notifications.coins.CoinsNotification.Companion.marketStatusId
import com.mathroda.notifications.util.getNotificationManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CoinsNotification @Inject constructor(
    @ApplicationContext private val context: Application,
    private val channel: CoinsNotificationChannel
) {

    fun show(
        id: Int,
        title: String,
        description: String,
        state: UserState
    ) {
        val builder = buildNotification(
            title = title,
            description = description,
            state = state
        )
        context.getNotificationManager()?.notify(id, builder.build())
    }

    private fun buildNotification(
        title: String,
        description: String,
        state: UserState
    ) =
        NotificationCompat.Builder(context, channel.getChannelId()).apply {
            setSmallIcon(R.mipmap.ic_dashcoin)
            setContentTitle(title)
            setContentText(description)
            setAutoCancel(true)
            setContentIntent(buildPendingIntent(state))
        }

    private fun buildPendingIntent(state: UserState): PendingIntent {
        val openCoinIntent = Intent(
            Intent.ACTION_VIEW,
            userStateUri(state)
        )

        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(openCoinIntent)
            getPendingIntent(
                REQUEST_CODE_OPEN_TASK,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }

    private fun userStateUri(state: UserState): Uri {
        return when(state) {
            is UserState.UnauthedUser -> DestinationsDeepLink.getHomeUri()
            is UserState.AuthedUser -> DestinationsDeepLink.getHomeUri()
            is UserState.PremiumUser -> DestinationsDeepLink.getHomeUri()
        }
    }

    companion object {
        private const val REQUEST_CODE_OPEN_TASK = 1_121_111
        const val marketStatusId = Int.MAX_VALUE

    }
}

fun CoinsNotification.showPositive(
    state: UserState
) {
    show(
        id = marketStatusId,
        title = Constants.TITLE,
        description = Constants.DESCRIPTION_POSITIVE,
        state = state
    )
}

fun CoinsNotification.showNegative(
    state: UserState
) {
    show(
        id = marketStatusId,
        title = Constants.TITLE,
        description = Constants.DESCRIPTION_NEGATIVE,
        state = state
    )
}