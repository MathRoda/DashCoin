package com.mathroda.datasource.di

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.mathroda.datasource.datastore.createDataStore
import com.mathroda.datasource.datastore.dataStoreFileName
import com.mathroda.datasource.google_service.GoogleAuthProviderImpl
import com.mathroda.domain.GoogleAuthCredentials
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal const val webId = "438848692298-5d0h12bsnnc78jg0ntop8ebps964feje.apps.googleusercontent.com"
actual fun platformModule() = module {
    single { initDataSore(androidContext()) }
    single { GoogleAuthCredentials(webId) }
    single { CredentialManager.create(androidContext()) }
    single { GoogleAuthProviderImpl(get(), get(), get()) }
}

fun initDataSore(context: Context): DataStore<Preferences> = createDataStore(
    producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath }
)
