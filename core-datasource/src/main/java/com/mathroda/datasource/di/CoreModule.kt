package com.mathroda.datasource.di

import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.core.DashCoinRepositoryImpl
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
        api: com.mathroda.network.DashCoinApi
    ): DashCoinRepository {
        return DashCoinRepositoryImpl(api)
    }

}