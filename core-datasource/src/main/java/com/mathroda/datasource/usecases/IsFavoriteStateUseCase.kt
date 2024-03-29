package com.mathroda.datasource.usecases

import com.mathroda.core.state.IsFavoriteState
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.domain.model.FavoriteCoin
import javax.inject.Inject

class IsFavoriteStateUseCase @Inject constructor(
    private val dashCoinRepository: DashCoinRepository
) {
    operator fun invoke(
        coin: FavoriteCoin
    ): IsFavoriteState {
        val result = dashCoinRepository.getFavoriteCoinByIdLocal(coin.coinId)

        if (result != null) {
            return IsFavoriteState.Favorite
        }

        return IsFavoriteState.NotFavorite
    }
}