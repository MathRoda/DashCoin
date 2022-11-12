package com.mathroda.datasource.core

import coil.network.HttpException
import com.mathroda.core.util.Resource
import com.mathroda.domain.CoinById
import com.mathroda.network.DashCoinApi
import com.mathroda.network.dto.toChart
import com.mathroda.network.dto.toCoinDetail
import com.mathroda.network.dto.toCoins
import com.mathroda.network.dto.toNewsDetail
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class DashCoinRepositoryImpl @Inject constructor(
    private val api: DashCoinApi
): DashCoinRepository {

    //api requests functions implementation
    override fun getCoins(): Flow<Resource<List<com.mathroda.domain.Coins>>> = callbackFlow {
        try {
            this.trySend(Resource.Loading())
            val coins = api.getCoins().coins.map { it.toCoins() }
            this.trySend(Resource.Success(coins) )
        } catch (e: HttpException) {
            this.trySend(Resource.Error(e.localizedMessage?: "Unexpected Error"))
        } catch (e: IOException) {
            this.trySend(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
        awaitClose { this.cancel() }
    }

    override fun getCoinById(coinId: String): Flow<Resource<CoinById>> = flow {
        try {
            emit(Resource.Loading())
            val coin = api.getCoinById(coinId).coin.toCoinDetail()
            emit(Resource.Success(coin))
        }catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage?: "An unexpected error"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }

    override fun getChartsData(coinId: String): Flow<Resource<com.mathroda.domain.Charts>> = flow {

        try {
            emit(Resource.Loading())
            val coins = api.getChartsData(coinId).toChart()
            emit(Resource.Success(coins) )
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage?: "Unexpected Error"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }

    override fun getNews(filter: String): Flow<Resource<List<com.mathroda.domain.NewsDetail>>> = flow {
        try {
            emit(Resource.Loading())
            val coin = api.getNews(filter).news.map { it.toNewsDetail() }
            emit(Resource.Success(coin))
        }catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage?: "An unexpected error"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }
}