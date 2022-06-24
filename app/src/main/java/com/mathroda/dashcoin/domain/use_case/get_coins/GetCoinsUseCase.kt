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
            emit(Resource.Loading<List<Coins>>())
            val coins = repository.getCoins().coins.map { it.toCoins() }
            emit(Resource.Success<List<Coins>>(coins) )
        } catch (e: HttpException) {
            emit(Resource.Error<List<Coins>>(e.localizedMessage?: "Unexpected Error"))
        } catch (e: IOException) {
            emit(Resource.Error<List<Coins>>("Couldn't reach server. Check your internet connection"))
        }
    }
}