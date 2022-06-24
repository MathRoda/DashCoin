package com.mathroda.dashcoin.domain.use_case.get_coin

import com.mathroda.dashcoin.common.Resource
import com.mathroda.dashcoin.data.remot.dto.toCoinDetail
import com.mathroda.dashcoin.domain.model.CoinDetail
import com.mathroda.dashcoin.domain.repository.DashCoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCoinUseCase @Inject constructor(
    private val repository: DashCoinRepository
) {

    operator fun invoke(coinId: String): Flow<Resource<CoinDetail>> = flow {
        try {
            emit(Resource.Loading<CoinDetail>())
            val coin = repository.getCoinById(coinId).toCoinDetail()
            emit(Resource.Success<CoinDetail>(coin))
        }catch (e: HttpException) {
            emit(Resource.Error<CoinDetail>(e.localizedMessage?: "An unexpected error"))
        } catch (e: IOException) {
            emit(Resource.Error<CoinDetail>("Couldn't reach server. Check your internet connection"))
        }
    }
}