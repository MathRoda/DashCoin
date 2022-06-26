package com.mathroda.dashcoin.domain.repository

import com.mathroda.dashcoin.data.remote.dto.*
import com.mathroda.dashcoin.domain.model.CoinById
import com.mathroda.dashcoin.domain.model.Coins
import kotlinx.coroutines.flow.Flow

interface DashCoinRepository {

    // api requests
    suspend fun getCoins(): CoinsDto

    suspend fun getCoinById(coinId: String): CoinDetailDto

    suspend fun getChartsData(coinId: String): ChartDto

    suspend fun getNews(filter: String): NewsDto

    // database functions
    suspend fun insertCoin(coins: CoinById)

    suspend fun deleteCoin(coins: CoinById)

    fun getAllCoins(): Flow<List<CoinById>>

}