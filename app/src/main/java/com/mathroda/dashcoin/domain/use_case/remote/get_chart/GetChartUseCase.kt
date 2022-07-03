package com.mathroda.dashcoin.domain.use_case.remote.get_chart

import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.data.dto.toChart
import com.mathroda.dashcoin.domain.model.Charts
import com.mathroda.dashcoin.domain.repository.DashCoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetChartUseCase @Inject constructor(
    private val repository: DashCoinRepository
) {

    operator fun invoke(coinId: String): Flow<Resource<Charts>> = flow {

        try {
            emit(Resource.Loading())
            val coins = repository.getChartsData(coinId).toChart()
            emit(Resource.Success(coins) )
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage?: "Unexpected Error"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }
}