package com.mathroda.datasource.usecases

import com.mathroda.core.state.IsFavoriteState
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.domain.model.FavoriteCoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IsFavoriteStateUseCase @Inject constructor(
    private val dashCoinRepository: DashCoinRepository
) {
    operator fun invoke(
        coin: FavoriteCoin
    ): Flow<Resource<IsFavoriteState>> {
        return flow {
            emit(Resource.Loading())

            val result = dashCoinRepository.getFavoriteCoinByIdLocal(coin.coinId)

            if (result != null) {
                emit(Resource.Success(IsFavoriteState.Favorite))
            }
        }
    }
}