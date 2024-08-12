package com.mathroda.datasource.core

import com.example.cache.dbo.favoritecoins.FavoriteCoinsDao
import com.example.cache.dbo.favoritecoins.toDomain
import com.example.cache.dbo.favoritecoins.toEntity
import com.example.cache.dbo.user.UserDao
import com.example.cache.dbo.user.toDashCoinUser
import com.example.cache.dbo.user.toUserEntity
import com.example.cache.mapper.toDomain
import com.example.cache.mapper.toEntity
import com.mathroda.core.util.Resource
import com.mathroda.domain.model.ChartTimeSpan
import com.mathroda.domain.model.Charts
import com.mathroda.domain.model.CoinById
import com.mathroda.domain.model.Coins
import com.mathroda.domain.model.DashCoinUser
import com.mathroda.domain.model.FavoriteCoin
import com.mathroda.domain.model.NewsDetail
import com.mathroda.domain.model.NewsType
import com.mathroda.network.DashCoinApi
import com.mathroda.network.dto.ChartDto
import com.mathroda.network.dto.toChart
import com.mathroda.network.dto.toCoinDetail
import com.mathroda.network.dto.toCoins
import com.mathroda.network.dto.toNewsDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException

class DashCoinRepositoryImpl (
    private val api: DashCoinApi,
    private val favoriteCoinsDao: FavoriteCoinsDao,
    private val userDao: UserDao
) : DashCoinRepository {

    //api requests functions implementation
    override fun getCoinsRemote(skip: Int): Flow<Resource<List<Coins>>> = flow {
        try {
            emit(Resource.Loading())
            val coins = api.getCoins(
                page = skip,
                ).result.map { it.toCoins() }
            emit(Resource.Success(coins))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected Error"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }

    override fun getCoinByIdRemoteFlow(coinId: String): Flow<Resource<CoinById>> = flow {
        try {
            emit(Resource.Loading())
            val coin = api.getCoinById(
                coinId = coinId,
            ).toCoinDetail()
            emit(Resource.Success(coin))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }

    override suspend fun getCoinByIdRemote(coinId: String): CoinById {
        return api.getCoinById(
            coinId = coinId,
        ).toCoinDetail()
    }

    override fun getChartsDataRemote(coinId: String, period: ChartTimeSpan): Flow<Resource<Charts>> = flow {

        try {
            emit(Resource.Loading())
            val result = api.getChartsData(
                coinId = coinId,
                period = period.value,
            )

            val coins = ChartDto(chart = result).toChart()
            emit(Resource.Success(coins))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected Error"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }

    override fun getNewsRemote(filter: NewsType): Flow<Resource<List<NewsDetail>>> =
        flow {
            try {
                emit(Resource.Loading())
                val coin = api.getNews(
                    filter = filter.value,
                ).map { it.toNewsDetails() }
                emit(Resource.Success(coin))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error"))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection"))
            }
        }

    override fun getFlowFavoriteCoins(): Flow<List<FavoriteCoin>> {
        return favoriteCoinsDao.getAllFavoriteCoins().map { it.toDomain() }
    }

    override fun getFavoriteCoinByIdLocal(coinId: String): FavoriteCoin? {
        return favoriteCoinsDao.getFavoriteCoinById(coinId)?.toDomain()
    }

    override suspend fun upsertFavoriteCoin(coin: FavoriteCoin) {
        favoriteCoinsDao.upsertFavoriteCoin(coin.toEntity())
    }

    override suspend fun removeFavoriteCoin(coin: FavoriteCoin) {
        coin.toEntity().coinId.run { favoriteCoinsDao.deleteFavoriteCoin(this) }
    }

    override suspend fun addAllFavoriteCoins(coins: List<FavoriteCoin>) {
        favoriteCoinsDao.insertAllFavoriteCoins(coins.toEntity())
    }

    override fun getDashCoinUser(): DashCoinUser? {
        return userDao.getUser()?.toDashCoinUser()
    }

    override suspend fun cacheDashCoinUser(user: DashCoinUser) {
        userDao.insertUser(user.toUserEntity())
    }

    override suspend fun removeDashCoinUserRecord() {
        return userDao.deleteUser()
    }

    override fun getFavoriteCoinsCount(): Flow<Int> {
        return favoriteCoinsDao.getFavoriteCoinsCount()
    }

    override fun isUserPremiumLocal(): Boolean {
        return userDao.getUser()?.toDashCoinUser()?.isUserPremium() == true
    }

    override suspend fun removeAllFavoriteCoins() {
        return favoriteCoinsDao.removeAllFavoriteCoins()
    }
}