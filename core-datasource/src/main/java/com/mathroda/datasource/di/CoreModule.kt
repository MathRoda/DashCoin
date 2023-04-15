package com.mathroda.datasource.di

import com.example.cache.dbo.favoritecoins.FavoriteCoinsDao
import com.example.cache.dbo.user.UserDao
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.core.DashCoinRepositoryImpl
import com.mathroda.network.DashCoinApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun providesDashCoinRepository(
        api: DashCoinApi,
        favoriteCoinDao: FavoriteCoinsDao,
        userDao: UserDao
    ): DashCoinRepository {
        return DashCoinRepositoryImpl(
            api,
            favoriteCoinDao,
            userDao
        )
    }

}