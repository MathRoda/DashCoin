@file:OptIn(ExperimentalForeignApi::class, ExperimentalForeignApi::class)

package com.mathroda.datasource.di

import com.mathroda.datasource.google_service.GoogleAuthProvider
import com.mathroda.datasource.google_service.GoogleAuthProviderImpl
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module

actual fun platformModule() = module {
    single<GoogleAuthProvider> { GoogleAuthProviderImpl() }
}
