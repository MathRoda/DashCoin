package com.mathroda.dashcoin.data.repository

import com.mathroda.dashcoin.data.remot.DashCoinApi
import com.mathroda.dashcoin.data.remot.dto.ChartDto
import com.mathroda.dashcoin.data.remot.dto.Coin
import com.mathroda.dashcoin.data.remot.dto.CoinDto
import com.mathroda.dashcoin.data.remot.dto.NewsDto
import com.mathroda.dashcoin.domain.repository.DashCoinRepository
import javax.inject.Inject

class DashCoinRepositoryImpl @Inject constructor(
    private val api: DashCoinApi
): DashCoinRepository {

    override suspend fun getCoins(): CoinDto {
        return api.getCoins()
    }

    override suspend fun getCoinById(coinId: String): Coin {
        return api.getCoinById(coinId)
    }

    override suspend fun getChartsData(coinId: String): ChartDto {
       return api.getChartsData(coinId)
    }

    override suspend fun getNews(filter: String): NewsDto {
        return api.getNews(filter)
    }
}