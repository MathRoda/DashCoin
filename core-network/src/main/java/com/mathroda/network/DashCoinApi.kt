package com.mathroda.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DashCoinApi {

    @GET("v1/coins")
    suspend fun getCoins(
        @Query("currency") currency: String = "USD",
        @Query("skip") skip: Int = 0
    ): com.mathroda.network.dto.CoinsDto

    @GET("v1/coins/{coinId}")
    suspend fun getCoinById(
        @Path("coinId") coinId: String
    ): com.mathroda.network.dto.CoinDetailDto

    @GET("v1/charts")
    suspend fun getChartsData(
        @Query("coinId") coinId: String,
        @Query("period") period: String = "24h" //available periods - 24h | 1w | 1m | 3m | 6m | 1y | all
    ): com.mathroda.network.dto.ChartDto

    @GET("v1/news/{filter}")
    suspend fun getNews(
        @Path("filter") filter: String,
        /**
         * available filters
         * 1. handpicked
         * 2. trending
         * 3. latest
         * 4. bullish
         * 5. bearish
         */
        @Query("limit") limit: Int = 20,
        @Query("skip") skip: Int = 0
    ): com.mathroda.network.dto.NewsDto
}