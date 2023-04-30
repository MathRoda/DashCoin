package com.mathroda.datasource.fake

import com.example.cache.dbo.favoritecoins.FavoriteCoinEntity
import com.example.cache.dbo.favoritecoins.toDomain
import com.example.cache.dbo.favoritecoins.toEntity
import com.example.cache.mapper.toEntity
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.domain.model.ChartTimeSpan
import com.mathroda.domain.model.Charts
import com.mathroda.domain.model.CoinById
import com.mathroda.domain.model.Coins
import com.mathroda.domain.model.DashCoinUser
import com.mathroda.domain.model.FavoriteCoin
import com.mathroda.domain.model.NewsDetail
import com.mathroda.domain.model.NewsType
import kotlinx.coroutines.flow.Flow

class DashCoinRepositoryFake: DashCoinRepository {
    private val favoriteCoinDB = mutableListOf<FavoriteCoinEntity?>()

    override fun getCoinsRemote(skip: Int): Flow<Resource<List<Coins>>> {
        TODO("Not yet implemented")
    }

    override fun getCoinByIdRemote(coinId: String): Flow<Resource<CoinById>> {
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
        TODO("Not yet implemented")
    }

    override fun getFavoriteCoinByIdLocal(coinId: String): FavoriteCoin? {
        return try {
            favoriteCoinDB.last { it?.coinId == coinId }?.toDomain()
        } catch (e: Exception) {
            return null
        }
    }

    override suspend fun addFavoriteCoin(coin: FavoriteCoin) {
        favoriteCoinDB.add(coin.toEntity())
    }

    override suspend fun removeFavoriteCoin(coin: FavoriteCoin) {
        favoriteCoinDB.remove(coin.toEntity())
    }

    override suspend fun addAllFavoriteCoins(coins: List<FavoriteCoin>) {
        favoriteCoinDB.addAll(coins.toEntity())
    }

    override fun getDashCoinUser(): Flow<DashCoinUser?> {
        TODO("Not yet implemented")
    }

    override suspend fun cacheDashCoinUser(user: DashCoinUser) {
        TODO("Not yet implemented")
    }

    override suspend fun removeDashCoinUserRecord() {
        TODO("Not yet implemented")
    }

    override fun getFavoriteCoinsCount(): Flow<Int> {
        TODO("Not yet implemented")
    }

    override fun isUserPremiumLocal(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun removeAllFavoriteCoins() {
        TODO("Not yet implemented")
    }
}