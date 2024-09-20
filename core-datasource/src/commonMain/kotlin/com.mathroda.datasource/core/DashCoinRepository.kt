package com.mathroda.datasource.core

import com.mathroda.core.util.Resource
import com.mathroda.domain.ChartTimeSpan
import com.mathroda.domain.Charts
import com.mathroda.domain.CoinById
import com.mathroda.domain.Coins
import com.mathroda.domain.DashCoinUser
import com.mathroda.domain.FavoriteCoin
import com.mathroda.domain.NewsDetail
import com.mathroda.domain.NewsType
import kotlinx.coroutines.flow.Flow

interface DashCoinRepository {

    // api requests
    fun getCoinsRemote(skip: Int): Flow<Resource<List<Coins>>>

    fun getCoinByIdRemoteFlow(coinId: String): Flow<Resource<CoinById>>

    suspend fun getCoinByIdRemote(coinId: String): CoinById

    fun getChartsDataRemote(coinId: String, period: ChartTimeSpan): Flow<Resource<Charts>>

    fun getNewsRemote(filter: NewsType): Flow<Resource<List<NewsDetail>>>

    fun getFlowFavoriteCoins(): Flow<List<FavoriteCoin>>

    suspend fun getFavoriteCoinByIdLocal(
        coinId: String
    ): FavoriteCoin?

    suspend fun upsertFavoriteCoin(coin: FavoriteCoin)

    suspend fun removeFavoriteCoin(coin: FavoriteCoin)

    suspend fun addAllFavoriteCoins(coins: List<FavoriteCoin>)

    suspend fun getDashCoinUser(): DashCoinUser?
    fun getDashCoinUserFlow(): Flow<DashCoinUser?>

    suspend fun cacheDashCoinUser(user: DashCoinUser)

    suspend fun removeDashCoinUserRecord()

    fun getFavoriteCoinsCount(): Flow<Int>

    suspend fun isUserPremiumLocal(): Boolean

    suspend fun removeAllFavoriteCoins()

}