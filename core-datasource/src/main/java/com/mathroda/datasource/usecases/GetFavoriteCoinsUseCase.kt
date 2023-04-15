package com.mathroda.datasource.usecases

import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.domain.model.FavoriteCoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFavoriteCoinsUseCase @Inject constructor(
    private val dashCoinRepository: DashCoinRepository,
    private val firebaseRepository: FirebaseRepository
) {

    operator fun invoke(): Flow<Resource<List<FavoriteCoin>>> {
        return flow {
            emit(Resource.Loading())
            dashCoinRepository.getFavoriteCoins().collect { coins ->
                if (coins.isNotEmpty()) {
                    emit(Resource.Success(coins))
                }

                if (coins.isEmpty()) {
                    firebaseRepository.getCoinFavorite().collect { result ->
                        when(result) {
                            is Resource.Success -> {
                                result.data?.let {
                                    dashCoinRepository.addAllFavoriteCoins(it)
                                    emit(Resource.Success(it))
                                }
                            }
                            is Resource.Error -> emit(Resource.Error(result.message ?: "unknown error"))
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}