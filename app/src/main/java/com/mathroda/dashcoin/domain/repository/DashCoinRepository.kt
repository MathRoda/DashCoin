package com.mathroda.dashcoin.domain.repository

import com.mathroda.dashcoin.data.remot.dto.ChartDto
import com.mathroda.dashcoin.data.remot.dto.Coin
import com.mathroda.dashcoin.data.remot.dto.CoinDto
import com.mathroda.dashcoin.data.remot.dto.NewsDto

interface DashCoinRepository {

    suspend fun getCoins(): CoinDto

    suspend fun getCoinById(coinId: String): Coin

    suspend fun getChartsData(coinId: String): ChartDto

    suspend fun getNews(filter: String): NewsDto
}