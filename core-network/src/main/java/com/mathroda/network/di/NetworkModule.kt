package com.mathroda.network.di

import com.mathroda.core.util.Constants.BASE_URL
import com.mathroda.network.API_KEY_COIN_STATS
import com.mathroda.network.DashCoinApi
import com.mathroda.network.DashCoinClient
import com.mathroda.network.interceptor.OkhttpInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

//TODO: get your API Key https://openapi.coinstats.app/login
private const val API_KEY: String = API_KEY_COIN_STATS
val networkModule = module {
    single<HttpClient> {
        val engine = OkHttp.create {
            addInterceptor(OkhttpInterceptor())
        }
        HttpClient(engine).config {
            install(ContentNegotiation) {
                json(
                    Json { ignoreUnknownKeys = true }
                )
            }

            defaultRequest {
                url(BASE_URL)
                header("accept", "application/json")
                header("X-API-KEY", API_KEY)
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
    }

    single<DashCoinApi> { DashCoinClient(get()) }
}