package com.mathroda.dashcoin.domain.use_case.get_chart

import com.mathroda.dashcoin.common.Resource
import com.mathroda.dashcoin.data.remot.dto.toChart
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
            emit(Resource.Loading<Charts>())
            val coins = repository.getChartsData(coinId).toChart()
            emit(Resource.Success<Charts>(coins) )
        } catch (e: HttpException) {
            emit(Resource.Error<Charts>(e.localizedMessage?: "Unexpected Error"))
        } catch (e: IOException) {
            emit(Resource.Error<Charts>("Couldn't reach server. Check your internet connection"))
        }
    }
}