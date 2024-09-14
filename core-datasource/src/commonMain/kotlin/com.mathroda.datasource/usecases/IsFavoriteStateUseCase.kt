package com.mathroda.datasource.usecases

import com.mathroda.core.state.IsFavoriteState
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.domain.FavoriteCoin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class IsFavoriteStateUseCase(
    private val dashCoinRepository: DashCoinRepository
) {
    suspend operator fun invoke(
        coin: FavoriteCoin
    ): IsFavoriteState {
        return withContext(Dispatchers.IO) {
            val result = dashCoinRepository.getFavoriteCoinByIdLocal(coin.coinId)

            if (result != null) {
                return@withContext IsFavoriteState.Favorite
            }

            IsFavoriteState.NotFavorite
        }
    }
}