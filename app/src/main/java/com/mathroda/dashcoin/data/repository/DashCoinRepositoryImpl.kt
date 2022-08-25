package com.mathroda.dashcoin.data.repository

import com.mathroda.dashcoin.data.dto.ChartDto
import com.mathroda.dashcoin.data.dto.CoinDetailDto
import com.mathroda.dashcoin.data.dto.CoinsDto
import com.mathroda.dashcoin.data.dto.NewsDto
import com.mathroda.dashcoin.data.remote.DashCoinApi
import com.mathroda.dashcoin.domain.repository.DashCoinRepository
import javax.inject.Inject

class DashCoinRepositoryImpl @Inject constructor(
    private val api: DashCoinApi
): DashCoinRepository {

    //api requests functions implementation
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