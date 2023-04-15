package com.mathroda.workmanger.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mathroda.core.state.UserState
import com.mathroda.core.util.Constants
import com.mathroda.core.util.Constants.BITCOIN_ID
import com.mathroda.core.util.Constants.DESCRIPTION_POSITIVE
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.usecases.DashCoinUseCases
import com.mathroda.notifications.CoinsNotification
import com.mathroda.workmanger.util.is5PercentDown
import com.mathroda.workmanger.util.is5PercentUp
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

@HiltWorker
class DashCoinWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val dashCoinRepository: DashCoinRepository,
    private val dashCoinUseCases: DashCoinUseCases,
    private val notification: CoinsNotification
) : CoroutineWorker(context, workerParameters) {
    private val marketStatusId = Int.MAX_VALUE
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

       return@withContext try {
            dashCoinUseCases.userStateProvider(
               function = {}
            ).collect { state ->
                when(state) {
                    is UserState.UnauthedUser -> regularUserNotification(state)
                    is UserState.AuthedUser -> regularUserNotification(state)
                    is UserState.PremiumUser -> premiumUserNotification(state)
                }
            }
            Result.success()
        } catch (exception: Exception) {
            Result.failure()
        }
    }

    private suspend fun regularUserNotification(state: UserState) {
            dashCoinRepository.getCoinByIdRemote(BITCOIN_ID).firstOrNull()?.let { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { coin ->
                            if (coin.priceChange1d > 0) {
                                notification.show(
                                    title = Constants.TITLE,
                                    description = DESCRIPTION_POSITIVE,
                                    id = marketStatusId,
                                    state = state
                                )
                                return
                            }

                            if (coin.priceChange1d < 0) {
                                notification.show(
                                    title = Constants.TITLE,
                                    description = Constants.DESCRIPTION_NEGATIVE,
                                    id = marketStatusId,
                                    state = state
                                )

                                return
                            }
                        }
                    }
                    else -> {}
                }
            }
    }

    private suspend fun premiumUserNotification(state: UserState) {
            dashCoinUseCases.getAllFavoriteCoins().firstOrNull()?.let { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.onEach { coin ->
                            coin.priceChanged1d.let { marketChange ->
                                if (marketChange.is5PercentUp()) {
                                    notification.show(
                                        title = coin.name ,
                                        description = Constants.DESCRIPTION_MARKET_CHANGE_POSITIVE,
                                        id = coin.rank,
                                        state = state
                                    )
                                    return
                                }

                                if (marketChange.is5PercentDown()) {
                                    notification.show(
                                        title = coin.name,
                                        description = Constants.DESCRIPTION_MARKET_CHANGE_NEGATIVE,
                                        id = coin.rank,
                                        state = state
                                    )
                                    return
                                }
                            }
                        }
                    }
                    else -> {}
                }
            }
    }
}