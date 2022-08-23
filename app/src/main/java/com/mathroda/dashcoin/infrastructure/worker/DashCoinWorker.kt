package com.mathroda.dashcoin.infrastructure.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mathroda.dashcoin.core.util.Constants
import com.mathroda.dashcoin.domain.repository.DashCoinRepository
import com.mathroda.dashcoin.infrastructure.notification.NotificationUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DashCoinWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val dashCoinRepository: DashCoinRepository,
): CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {

         return try {

             dashCoinRepository.getCoinById(Constants.BITCOIN_ID).let {
                 it.coin.let { coin ->
                     if (coin.priceChange1d >= 0 ) {
                         NotificationUtils.showNotification(
                             context = applicationContext,
                             title = Constants.TITLE,
                             description = Constants.DESCRIPTION_POSITIVE
                         )
                     } else {
                         NotificationUtils.showNotification(
                             context = applicationContext,
                             title = Constants.TITLE,
                             description = Constants.DESCRIPTION_NEGATIVE
                         )
                     }
                 }
             }

            Result.success()
        }catch (exception: Exception) {
            Result.failure()
        }
    }
}