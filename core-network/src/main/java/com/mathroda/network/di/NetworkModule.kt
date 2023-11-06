package com.mathroda.network.di

import com.mathroda.core.util.Constants.BASE_URL
import com.mathroda.network.DashCoinApi
import com.mathroda.network.interceptor.OkhttpInterceptor
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

    private const val API_KEY: String = //get your API Key https://openapi.coinstats.app/login

    @Provides
    @Singleton
    fun providesOkhttpInterceptor(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(OkhttpInterceptor())
            .addNetworkInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                requestBuilder.header("Content-Type", "application/json")
                requestBuilder.header("X-API-KEY", API_KEY)
                chain.proceed(requestBuilder.build())
            }
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