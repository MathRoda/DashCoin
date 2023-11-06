package com.mathroda.network

import com.mathroda.network.dto.CoinDetailDto
import com.mathroda.network.dto.CoinsDto
import com.mathroda.network.dto.NewsDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DashCoinApi {
    @GET("/coins")
    suspend fun getCoins(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 10,
        @Query("currency") currency: String = "USD",
        /**
         * @Unused parameter
         */
        //@Query("blockchain") blockchain: String = "ethereum"
    ): CoinsDto

    @GET("/coins/{coinId}")
    suspend fun getCoinById(
        @Path("coinId") coinId: String,
        @Query("currency") currency: String = "USD",
    ): CoinDetailDto

    @GET("/coins/{coinId}/charts")
    suspend fun getChartsData(
        @Path("coinId") coinId: String,
        @Query("period") period: String, //available periods - 24h | 1w | 1m | 3m | 6m | 1y | all
    ): List<List<Double>>

    @GET("/news/type/{type}")
    suspend fun getNews(
        @Path("type") filter: String,
        /**
         * available filters
         * 1. handpicked
         * 2. trending
         * 3. latest
         * 4. bullish
         * 5. bearish
         */
        @Query("limit") limit: Int = 20,
        @Query("page") skip: Int = 1
    ): NewsDto
}