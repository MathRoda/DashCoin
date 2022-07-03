package com.mathroda.dashcoin.domain.use_case.remote.get_coin

import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.data.dto.toCoinDetail
import com.mathroda.dashcoin.domain.model.CoinById
import com.mathroda.dashcoin.domain.repository.DashCoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCoinUseCase @Inject constructor(
    private val repository: DashCoinRepository
) {

    operator fun invoke(coinId: String): Flow<Resource<CoinById>> = flow {
        try {
            emit(Resource.Loading())
            val coin = repository.getCoinById(coinId).coin.toCoinDetail()
            emit(Resource.Success(coin))
        }catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage?: "An unexpected error"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }
}