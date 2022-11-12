package com.mathroda.datasource.core

import com.mathroda.core.util.Resource
import com.mathroda.domain.CoinById
import com.mathroda.network.dto.ChartDto
import com.mathroda.network.dto.CoinDetailDto
import com.mathroda.network.dto.CoinsDto
import com.mathroda.network.dto.NewsDto
import kotlinx.coroutines.flow.Flow

interface DashCoinRepository {

    // api requests
     fun getCoins(): Flow<Resource<List<com.mathroda.domain.Coins>>>

     fun getCoinById(coinId: String): Flow<Resource<CoinById>>

     fun getChartsData(coinId: String): Flow<Resource<com.mathroda.domain.Charts>>

     fun getNews(filter: String): Flow<Resource<List<com.mathroda.domain.NewsDetail>>>


}