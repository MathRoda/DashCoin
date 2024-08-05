package com.mathroda.network.di

import com.mathroda.core.util.Constants.BASE_URL
import com.mathroda.network.DashCoinApi
import com.mathroda.network.interceptor.OkhttpInterceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val API_KEY: String = "" //TODO: get your API Key https://openapi.coinstats.app/login
val networkModule = module {
    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(OkhttpInterceptor())
            .addNetworkInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                requestBuilder.header("accept", "application/json")
                requestBuilder.header("X-API-KEY", API_KEY)
                chain.proceed(requestBuilder.build())
            }.build()
    }

    single<DashCoinApi> {
        Retrofit.Builder()
            .client(get())
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DashCoinApi::class.java)
    }
}