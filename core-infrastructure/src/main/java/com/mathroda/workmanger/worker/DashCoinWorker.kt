package com.mathroda.workmanger.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mathroda.core.state.UserState
import com.mathroda.core.util.Constants
import com.mathroda.core.util.Constants.BITCOIN_ID
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.usecases.DashCoinUseCases
import com.mathroda.notifications.coins.CoinsNotification
import com.mathroda.notifications.coins.showNegative
import com.mathroda.notifications.coins.showPositive
import com.mathroda.workmanger.util.is5PercentDown
import com.mathroda.workmanger.util.is5PercentUp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DashCoinWorker (
    context: Context,
    workerParameters: WorkerParameters,
    private val dashCoinRepository: DashCoinRepository,
    private val dashCoinUseCases: DashCoinUseCases,
    private val notification: CoinsNotification
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

       return@withContext try {
           when(val state = dashCoinUseCases.userStateProvider.invoke()) {
                is UserState.UnauthedUser -> regularUserNotification(state)
                is UserState.AuthedUser -> regularUserNotification(state)
                is UserState.PremiumUser -> premiumUserNotification()
            }

            Result.success()
        } catch (exception: Exception) {
            Result.failure()
        }
    }

    private suspend fun regularUserNotification(state: UserState) {
        val coin = dashCoinRepository.getCoinByIdRemote(BITCOIN_ID)
        if (coin.priceChange1d > 0){
            notification.showPositive(state)
        }

        if (coin.priceChange1d < 0) {
            notification.showNegative(state)
        }
    }

    private suspend fun premiumUserNotification() {
        dashCoinUseCases.getAllFavoriteCoins().collect { result ->
            if (result is Resource.Success) {
                if (result.data.isNullOrEmpty()) {
                    return@collect
                }

                result.data?.let { coins ->
                    coins.map {
                        if (it.priceChanged1d.is5PercentUp()) {
                            notification.show(
                                title = it.name ,
                                description = Constants.DESCRIPTION_MARKET_CHANGE_POSITIVE,
                                id = it.rank,
                                state = UserState.PremiumUser
                            )
                        }

                        if (it.priceChanged1d.is5PercentDown()) {
                            notification.show(
                                title = it.name ,
                                description = Constants.DESCRIPTION_MARKET_CHANGE_NEGATIVE,
                                id = it.rank,
                                state = UserState.PremiumUser
                            )
                        }
                    }
                }
            }
        }
    }
}