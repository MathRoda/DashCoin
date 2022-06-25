package com.mathroda.dashcoin.data.repository

import com.mathroda.dashcoin.data.remot.DashCoinApi
import com.mathroda.dashcoin.data.remot.dto.*
import com.mathroda.dashcoin.domain.repository.DashCoinRepository
import javax.inject.Inject

class DashCoinRepositoryImpl @Inject constructor(
    private val api: DashCoinApi
): DashCoinRepository {

    override suspend fun getCoins(): CoinsDto {
        return api.getCoins()
    }

    override suspend fun getCoinById(coinId: String): CoinDetailDto {
        return api.getCoinById(coinId)
    }

    override suspend fun getChartsData(coinId: String): ChartDto {
       return api.getChartsData(coinId)
    }

    override suspend fun getNews(filter: String): NewsDto {
        return api.getNews(filter)
    }
}