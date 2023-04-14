package com.mathroda.network.di

import com.mathroda.core.util.Constants.BASE_URL
import com.mathroda.network.DashCoinApi
import com.mathroda.network.interceptor.OkhttpInterceptor
import com.mathroda.network.DashCoinApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object NetworkModule {

    @Provides
    @Singleton
    fun providesOkhttpInterceptor(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(OkhttpInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun providesDashCoinApi(okHttpClient: OkHttpClient): DashCoinApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DashCoinApi::class.java)
    }
}