package com.mathroda.dashcoin.domain.use_case.remote.get_coins

import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.data.dto.toCoins
import com.mathroda.dashcoin.domain.model.Coins
import com.mathroda.dashcoin.domain.repository.DashCoinRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val repository: DashCoinRepository
) {

    operator fun invoke(): Flow<Resource<List<Coins>>> = callbackFlow {
        try {
            this.trySend(Resource.Loading())
            val coins = repository.getCoins().coins.map { it.toCoins() }
            this.trySend(Resource.Success(coins) )
        } catch (e: HttpException) {
            this.trySend(Resource.Error(e.localizedMessage?: "Unexpected Error"))
        } catch (e: IOException) {
            this.trySend(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
        awaitClose { this.cancel() }
    }
}