package com.mathroda.dashcoin.domain.repository

import com.mathroda.dashcoin.data.remot.dto.*

interface DashCoinRepository {

    suspend fun getCoins(): CoinsDto

    suspend fun getCoinById(coinId: String): CoinDetailDto

    suspend fun getChartsData(coinId: String): ChartDto

    suspend fun getNews(filter: String): NewsDto
}