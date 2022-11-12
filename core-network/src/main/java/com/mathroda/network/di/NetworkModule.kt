package com.mathroda.network.di

import com.mathroda.core.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object NetworkModule {
    @Provides
    @Singleton
    fun providesDashCoinApi(): com.mathroda.network.DashCoinApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(com.mathroda.network.DashCoinApi::class.java)
    }
}