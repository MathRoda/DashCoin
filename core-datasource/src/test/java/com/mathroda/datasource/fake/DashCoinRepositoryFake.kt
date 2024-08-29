package com.mathroda.datasource.fake

import com.mathroda.cache.dbo.favoritecoins.FavoriteCoinEntity
import com.mathroda.cache.dbo.favoritecoins.toDomain
import com.mathroda.cache.dbo.favoritecoins.toEntity
import com.mathroda.cache.dbo.user.UserEntity
import com.mathroda.cache.dbo.user.toDashCoinUser
import com.mathroda.cache.dbo.user.toUserEntity
import com.mathroda.cache.mapper.toEntity
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.domain.ChartTimeSpan
import com.mathroda.domain.Charts
import com.mathroda.domain.CoinById
import com.mathroda.domain.Coins
import com.mathroda.domain.DashCoinUser
import com.mathroda.domain.FavoriteCoin
import com.mathroda.domain.NewsDetail
import com.mathroda.domain.NewsType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DashCoinRepositoryFake: DashCoinRepository {
    private val favoriteCoinDB = mutableListOf<FavoriteCoinEntity?>()
    private val userDB = mutableListOf<UserEntity?>()

    override fun getCoinsRemote(skip: Int): Flow<Resource<List<Coins>>> {
        TODO("Not yet implemented")
    }

    override fun getCoinByIdRemoteFlow(coinId: String): Flow<Resource<CoinById>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCoinByIdRemote(coinId: String): CoinById {
        TODO("Not yet implemented")
    }

    override fun getChartsDataRemote(
        coinId: String,
        period: ChartTimeSpan
    ): Flow<Resource<Charts>> {
        TODO("Not yet implemented")
    }

    override fun getNewsRemote(filter: NewsType): Flow<Resource<List<NewsDetail>>> {
        TODO("Not yet implemented")
    }

    override fun getFlowFavoriteCoins(): Flow<List<FavoriteCoin>> {
        return flow {
            emit(
                favoriteCoinDB.map { it?.toDomain() ?: FavoriteCoin() }.toList()
            )
        }
    }

    override fun getFavoriteCoinByIdLocal(coinId: String): FavoriteCoin? {
        return try {
            favoriteCoinDB.last { it?.coinId == coinId }?.toDomain()
        } catch (e: Exception) {
            return null
        }
    }

    override suspend fun upsertFavoriteCoin(coin: FavoriteCoin) {
        favoriteCoinDB.add(coin.toEntity())
    }

    override suspend fun removeFavoriteCoin(coin: FavoriteCoin) {
        favoriteCoinDB.remove(coin.toEntity())
    }

    override suspend fun addAllFavoriteCoins(coins: List<FavoriteCoin>) {
        favoriteCoinDB.addAll(coins.toEntity())
    }

    override suspend fun getDashCoinUser(): DashCoinUser? {
            return userDB.first()?.toDashCoinUser()
    }

    override suspend fun cacheDashCoinUser(user: DashCoinUser) {
        userDB.add(user.toUserEntity())
    }

    override suspend fun removeDashCoinUserRecord() {
        userDB.clear()
    }

    override fun getFavoriteCoinsCount(): Flow<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun isUserPremiumLocal(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun removeAllFavoriteCoins() {
        favoriteCoinDB.clear()
    }
}