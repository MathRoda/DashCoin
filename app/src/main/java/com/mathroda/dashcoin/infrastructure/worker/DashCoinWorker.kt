package com.mathroda.dashcoin.infrastructure.worker

import android.content.Context
import androidx.compose.ui.graphics.drawscope.DrawContext
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mathroda.dashcoin.core.util.Constants
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.repository.DashCoinRepository
import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import com.mathroda.dashcoin.infrastructure.notification.NotificationUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collect

class DashCoinWorker @AssistedInject constructor(
    private val dashCoinRepository: DashCoinRepository,
    private val firebaseRepository: FirebaseRepository,
    @Assisted workerParameters: WorkerParameters,
    @Assisted context: Context
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {

        return try {

            var state = false

            val coins = dashCoinRepository.getAllCoins()

            firebaseRepository.getCoinFavorite().collect { result ->
                when(result) {
                    is Resource.Success -> {
                        result.data?.forEach { favorite ->
                            coins.collect {
                                it.forEach { coin ->
                                    if (favorite.name == coin.name && coin.price != 20000.0) {
                                        state = !state
                                    }
                                }
                            }

                            if (state) NotificationUtils.showNotification(
                                context = applicationContext,
                                title = Constants.TITLE,
                                description = Constants.DESCRIPTION
                            )
                        }
                    }
                    else -> {}
                }
            }
            Result.success()
        }catch (exception: Exception) {
            Result.failure()
        }
    }
}