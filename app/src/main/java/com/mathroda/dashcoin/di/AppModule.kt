package com.mathroda.dashcoin.di

import com.mathroda.dashcoin.common.Constants
import com.mathroda.dashcoin.data.remot.DashCoinApi
import com.mathroda.dashcoin.data.repository.DashCoinRepositoryImpl
import com.mathroda.dashcoin.domain.repository.DashCoinRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDashCoinApi(): DashCoinApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DashCoinApi::class.java)
    }

    @Provides
    @Singleton
    fun providesDashCoinRepository(api: DashCoinApi): DashCoinRepository{
        return DashCoinRepositoryImpl(api)
    }
}