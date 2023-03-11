package com.mathroda.datasource.core

import coil.network.HttpException
import com.mathroda.core.util.Resource
import com.mathroda.domain.ChartTimeSpan
import com.mathroda.domain.CoinById
import com.mathroda.domain.NewsType
import com.mathroda.network.DashCoinApi
import com.mathroda.network.dto.toChart
import com.mathroda.network.dto.toCoinDetail
import com.mathroda.network.dto.toCoins
import com.mathroda.network.dto.toNewsDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class DashCoinRepositoryImpl @Inject constructor(
    private val api: DashCoinApi
) : DashCoinRepository {

    //api requests functions implementation
    override fun getCoins(skip: Int): Flow<Resource<List<com.mathroda.domain.Coins>>> = flow {
        try {
            emit(Resource.Loading())
            val coins = api.getCoins(skip = skip).coins.map { it.toCoins() }
            emit(Resource.Success(coins))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected Error"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }

    override fun getCoinById(coinId: String): Flow<Resource<CoinById>> = flow {
        try {
            emit(Resource.Loading())
            val coin = api.getCoinById(coinId).coin.toCoinDetail()
            emit(Resource.Success(coin))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }

    override fun getChartsData(coinId: String, period: ChartTimeSpan): Flow<Resource<com.mathroda.domain.Charts>> = flow {

        try {
            emit(Resource.Loading())
            val coins = api.getChartsData(coinId, period.value).toChart()
            emit(Resource.Success(coins))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected Error"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }

    override fun getNews(filter: NewsType): Flow<Resource<List<com.mathroda.domain.NewsDetail>>> =
        flow {
            try {
                emit(Resource.Loading())
                val coin = api.getNews(filter.value).news.map { it.toNewsDetail() }
                emit(Resource.Success(coin))
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error"))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection"))
            }
        }
}