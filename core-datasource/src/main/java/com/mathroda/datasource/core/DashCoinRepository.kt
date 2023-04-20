package com.mathroda.datasource.core

import com.mathroda.core.util.Resource
import com.mathroda.domain.model.ChartTimeSpan
import com.mathroda.domain.model.Charts
import com.mathroda.domain.model.CoinById
import com.mathroda.domain.model.Coins
import com.mathroda.domain.model.DashCoinUser
import com.mathroda.domain.model.FavoriteCoin
import com.mathroda.domain.model.NewsDetail
import com.mathroda.domain.model.NewsType
import kotlinx.coroutines.flow.Flow

interface DashCoinRepository {

    // api requests
    fun getCoinsRemote(skip: Int): Flow<Resource<List<Coins>>>

    fun getCoinByIdRemote(coinId: String): Flow<Resource<CoinById>>

    fun getChartsDataRemote(coinId: String, period: ChartTimeSpan): Flow<Resource<Charts>>

    fun getNewsRemote(filter: NewsType): Flow<Resource<List<NewsDetail>>>

    fun getFlowFavoriteCoins(): Flow<List<FavoriteCoin>>

    fun getFavoriteCoinByIdLocal(
        coinId: String
    ): FavoriteCoin?

    suspend fun addFavoriteCoin(coin: FavoriteCoin)

    suspend fun removeFavoriteCoin(coin: FavoriteCoin)

    suspend fun addAllFavoriteCoins(coins: List<FavoriteCoin>)

    fun getDashCoinUser(): Flow<DashCoinUser?>

    suspend fun cacheDashCoinUser(user: DashCoinUser)

    suspend fun removeDashCoinUserRecord()

    fun getFavoriteCoinsCount(): Flow<Int>

    fun isUserPremiumLocal(): Flow<Boolean>

    suspend fun removeAllFavoriteCoins()

}