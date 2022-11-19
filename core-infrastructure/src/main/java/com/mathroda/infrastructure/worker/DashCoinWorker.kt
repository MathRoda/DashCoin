package com.mathroda.infrastructure.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mathroda.core.util.Constants
import com.mathroda.core.util.Constants.BITCOIN_ID
import com.mathroda.core.util.Constants.DESCRIPTION_POSITIVE
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.infrastructure.notification.NotificationUtils.showNotification
import com.mathroda.infrastructure.util.is5PercentDown
import com.mathroda.infrastructure.util.is5PercentUp
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class DashCoinWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val dashCoinRepository: DashCoinRepository,
    private val firebaseRepository: FirebaseRepository
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {

        return try {
            val marketStatusId = Int.MAX_VALUE

            firebaseRepository.isCurrentUserExist().collect { isExist ->
                if (!isExist) {
                    withContext(Dispatchers.IO) {
                        dashCoinRepository.getCoinById(BITCOIN_ID).collect { result ->
                            when (result) {
                                is Resource.Success -> {
                                    result.data?.let { coin ->
                                        if (coin.priceChange1d!! >= 0) {
                                            showNotification(
                                                context = applicationContext,
                                                title = Constants.TITLE,
                                                description = DESCRIPTION_POSITIVE,
                                                id = marketStatusId
                                            )
                                        } else {
                                            showNotification(
                                                context = applicationContext,
                                                title = Constants.TITLE,
                                                description = Constants.DESCRIPTION_NEGATIVE,
                                                id = marketStatusId
                                            )
                                        }
                                    }
                                }
                                else -> {}
                            }
                        }
                    }
                } else {
                    withContext(Dispatchers.IO) {
                        firebaseRepository.getAllFavoriteCoins().collect { result ->
                            when (result) {
                                is Resource.Loading -> {}
                                is Resource.Success -> {
                                    result.data?.onEach { coin ->
                                        dashCoinRepository.getCoinById(coin.id ?: BITCOIN_ID).collect {
                                            when(it) {
                                                is Resource.Success -> {
                                                    it.data?.priceChange1d?.let { marketChange ->
                                                        if (marketChange.is5PercentUp()) {
                                                            showNotification(
                                                                context = applicationContext,
                                                                title = coin.name ?: "Unknown coin",
                                                                description = Constants.DESCRIPTION_MARKET_CHANGE_POSITIVE,
                                                                id = coin.rank ?: 0
                                                            )
                                                        }

                                                        if (marketChange.is5PercentDown()) {
                                                            showNotification(
                                                                context = applicationContext,
                                                                title = coin.name ?: "Unknown coin",
                                                                description = Constants.DESCRIPTION_MARKET_CHANGE_NEGATIVE,
                                                                id = coin.rank ?: 0
                                                            )
                                                        }
                                                    }
                                                }
                                                else -> {}
                                            }
                                        }
                                    }
                                }
                                is Resource.Error -> {}
                            }
                        }
                    }
                }
            }

            Result.success()
        } catch (exception: Exception) {
            Result.failure()
        }
    }
}