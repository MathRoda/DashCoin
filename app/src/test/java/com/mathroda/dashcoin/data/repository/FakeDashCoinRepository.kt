package com.mathroda.dashcoin.data.repository

import com.mathroda.dashcoin.data.remote.DashCoinApi
import com.mathroda.dashcoin.data.remote.dto.ChartDto
import com.mathroda.dashcoin.data.remote.dto.CoinDetailDto
import com.mathroda.dashcoin.data.remote.dto.CoinsDto
import com.mathroda.dashcoin.data.remote.dto.NewsDto
import com.mathroda.dashcoin.domain.repository.DashCoinRepository
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FakeDashCoinRepository: DashCoinRepository {

    private val server = MockWebServer()

    private val apiMock: DashCoinApi = Retrofit.Builder()
        .baseUrl(server.url("http://local/"))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DashCoinApi::class.java)

    override suspend fun getCoins(): CoinsDto {
        return apiMock.getCoins()
    }

    override suspend fun getCoinById(coinId: String): CoinDetailDto {
        return apiMock.getCoinById(coinId)
    }

    override suspend fun getChartsData(coinId: String): ChartDto {
        return apiMock.getChartsData(coinId)
    }

    override suspend fun getNews(filter: String): NewsDto {
        return apiMock.getNews(filter)
    }
}

