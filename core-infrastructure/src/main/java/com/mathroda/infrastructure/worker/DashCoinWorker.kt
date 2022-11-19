package com.mathroda.infrastructure.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mathroda.core.util.Constants.BITCOIN_ID
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.domain.CoinById
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.onEach

@HiltWorker
class DashCoinWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val dashCoinRepository: DashCoinRepository,
    private val firebaseRepository: FirebaseRepository
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {

        return try {

            dashCoinRepository.getCoinById(BITCOIN_ID).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { coin ->
                            if (coin.priceChange1d!! >= 0) {
                                com.mathroda.infrastructure.notification.NotificationUtils.showNotification(
                                    context = applicationContext,
                                    title = com.mathroda.core.util.Constants.TITLE,
                                    description = com.mathroda.core.util.Constants.DESCRIPTION_POSITIVE
                                )
                            } else {
                                com.mathroda.infrastructure.notification.NotificationUtils.showNotification(
                                    context = applicationContext,
                                    title = com.mathroda.core.util.Constants.TITLE,
                                    description = com.mathroda.core.util.Constants.DESCRIPTION_NEGATIVE
                                )
                            }
                        }
                    }
                    else -> {}
                }
            }


            Result.success()
        } catch (exception: Exception) {
            Result.failure()
        }
    }
}