package com.mathroda.network

import com.mathroda.network.dto.CoinDetailDto
import com.mathroda.network.dto.CoinsDto
import com.mathroda.network.dto.NewsItem

interface DashCoinApi {
    suspend fun getCoins(
        page: Int,
        limit: Int = 10,
        currency: String = "USD",
        /**
         * @Unused parameter
         */
        //@Query("blockchain") blockchain: String = "ethereum"
    ): CoinsDto

    suspend fun getCoinById(
        coinId: String,
        currency: String = "USD",
    ): CoinDetailDto

    suspend fun getChartsData(
        coinId: String,
        period: String, //available periods - 24h | 1w | 1m | 3m | 6m | 1y | all
    ): List<List<Double>>

    suspend fun getNews(
        filter: String,
        /**
         * available filters
         * 1. handpicked
         * 2. trending
         * 3. latest
         * 4. bullish
         * 5. bearish
         */
        limit: Int = 20,
        skip: Int = 1
    ): List<NewsItem>
}