package com.mathroda.dashcoin.domain.use_case.get_coins

import com.mathroda.dashcoin.common.Resource
import com.mathroda.dashcoin.data.remot.dto.toCoins
import com.mathroda.dashcoin.domain.model.Coins
import com.mathroda.dashcoin.domain.repository.DashCoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val repository: DashCoinRepository
) {

    operator fun invoke(): Flow<Resource<List<Coins>>> = flow {

        try {
            emit(Resource.Loading())
            val coins = repository.getCoins().coins.map { it.toCoins() }
            emit(Resource.Success(coins) )
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage?: "Unexpected Error"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }
}