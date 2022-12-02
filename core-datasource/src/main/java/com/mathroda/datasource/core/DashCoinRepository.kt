package com.mathroda.datasource.core

import com.mathroda.core.util.Resource
import com.mathroda.domain.ChartTimeSpan
import com.mathroda.domain.CoinById
import kotlinx.coroutines.flow.Flow

interface DashCoinRepository {

    // api requests
    fun getCoins(): Flow<Resource<List<com.mathroda.domain.Coins>>>

    fun getCoinById(coinId: String): Flow<Resource<CoinById>>

    fun getChartsData(coinId: String, period: ChartTimeSpan): Flow<Resource<com.mathroda.domain.Charts>>

    fun getNews(filter: String): Flow<Resource<List<com.mathroda.domain.NewsDetail>>>


}