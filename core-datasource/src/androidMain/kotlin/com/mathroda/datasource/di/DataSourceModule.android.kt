package com.mathroda.datasource.di

import androidx.credentials.CredentialManager
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.datasource.google_service.GoogleAuthProviderImpl
import com.mathroda.domain.GoogleAuthCredentials
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal const val webId = "438848692298-5d0h12bsnnc78jg0ntop8ebps964feje.apps.googleusercontent.com"
actual fun platformModule() = module {
    single { GoogleAuthCredentials(webId) }
    single { CredentialManager.create(androidContext()) }
    single { GoogleAuthProviderImpl(get(), get(), get()) }
}

