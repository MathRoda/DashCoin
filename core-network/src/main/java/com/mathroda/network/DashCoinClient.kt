package com.mathroda.network

import com.mathroda.network.dto.CoinDetailDto
import com.mathroda.network.dto.CoinsDto
import com.mathroda.network.dto.NewsItem
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class DashCoinClient(private val client: HttpClient): DashCoinApi {
    override suspend fun getCoins(page: Int, limit: Int, currency: String): CoinsDto {
        return client.get("/coins") {
            parameter("page", page)
            parameter("limit", limit)
            parameter("currency", currency)
        }.body()
    }

    override suspend fun getCoinById(coinId: String, currency: String): CoinDetailDto {
        return client.get("/coins/$coinId") {
            parameter("currency", currency)
        }.body()
    }

    override suspend fun getChartsData(coinId: String, period: String): List<List<Double>> {
        return client.get("/coins/$coinId/charts") {
            parameter("period", period)
        }.body()
    }

    override suspend fun getNews(filter: String, limit: Int, skip: Int): List<NewsItem> {
        return client.get("/news/type/$filter") {
            parameter("limit", limit)
            parameter("page", skip)
        }.body()
    }
}