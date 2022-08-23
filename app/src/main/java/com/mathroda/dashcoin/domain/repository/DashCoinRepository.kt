package com.mathroda.dashcoin.domain.repository

import com.mathroda.dashcoin.data.dto.ChartDto
import com.mathroda.dashcoin.data.dto.CoinDetailDto
import com.mathroda.dashcoin.data.dto.CoinsDto
import com.mathroda.dashcoin.data.dto.NewsDto

interface DashCoinRepository {

    // api requests
    suspend fun getCoins(): CoinsDto

    suspend fun getCoinById(coinId: String): CoinDetailDto

    suspend fun getChartsData(coinId: String): ChartDto

    suspend fun getNews(filter: String): NewsDto


}