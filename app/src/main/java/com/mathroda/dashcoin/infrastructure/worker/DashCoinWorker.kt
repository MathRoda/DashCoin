package com.mathroda.dashcoin.infrastructure.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.viewModelScope
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mathroda.dashcoin.core.util.Constants
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.model.CoinById
import com.mathroda.dashcoin.domain.repository.DashCoinRepository
import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import com.mathroda.dashcoin.domain.use_case.DashCoinUseCases
import com.mathroda.dashcoin.infrastructure.notification.NotificationUtils
import com.mathroda.dashcoin.presentation.watchlist_screen.state.WatchListState
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltWorker
class DashCoinWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val dashCoinRepository: DashCoinRepository,
    private val firebaseRepository: FirebaseRepository,
    private val dashCoinUseCases: DashCoinUseCases
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

             firebaseRepository.isCurrentUserExist().collect { userExist ->

                 if (userExist) {
                     firebaseRepository.getCoinFavorite().onEach { result ->
                         when(result) {
                             is Resource.Loading -> {}
                             is Resource.Success -> {
                                 result.data?.map { coinById ->
                                     dashCoinUseCases.getCoin.invoke(coinById.id ?: "").collect { result ->
                                         when(result) {
                                             is Resource.Loading -> {}
                                             is Resource.Success -> {
                                                 firebaseRepository.addCoinFavorite(result.data ?: CoinById() )
                                             }
                                             is Resource.Error -> {}
                                         }
                                     }
                                 }
                             }
                             is Resource.Error -> {}
                         }
                     }
                 }
             }



            Result.success()
        }catch (exception: Exception) {
            Result.failure()
        }
    }
}