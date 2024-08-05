package com.mathroda.datasource.di

import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.mathroda.core.util.Constants.SIGN_IN_REQUEST
import com.mathroda.core.util.Constants.SIGN_UP_REQUEST
import com.mathroda.datasource.R
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.core.DashCoinRepositoryImpl
import com.mathroda.datasource.datastore.DataStoreRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.datasource.firebase.FirebaseRepositoryImpl
import com.mathroda.datasource.google_service.GoogleServicesRepository
import com.mathroda.datasource.google_service.GoogleServicesRepositoryImpl
import com.mathroda.datasource.usecases.CacheUserDataUseCase
import com.mathroda.datasource.usecases.DashCoinUseCases
import com.mathroda.datasource.usecases.GetFavoriteCoinsUseCase
import com.mathroda.datasource.usecases.IsFavoriteStateUseCase
import com.mathroda.datasource.usecases.ProvideUserStateUseCase
import com.mathroda.datasource.usecases.SignOutUseCase
import com.mathroda.domain.model.DashCoinUser
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module


val dataSourceModule = module {
    single<DashCoinRepository> {
        DashCoinRepositoryImpl(
            api = get(),
            favoriteCoinsDao = get(),
            userDao = get(),
        )
    }

    single { DataStoreRepository(androidApplication()) }

    single { FirebaseApp.initializeApp(androidApplication()) }
    single { Firebase.auth }
    single { Firebase.firestore }
    single { FirebaseStorage.getInstance() }
    single<FirebaseRepository> {
        FirebaseRepositoryImpl(
            firebaseAuth = get(),
            fireStore = get(),
            firebaseStorage = get()
        )
    }


    single {
        Identity.getSignInClient(androidApplication())
    }

    single(named(SIGN_IN_REQUEST)) {
        BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(androidApplication().getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    single(named(SIGN_UP_REQUEST)) {
        BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(androidApplication().getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
    }

    single<GoogleSignInOptions> {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(androidApplication().getString(R.string.web_client_id))
            .requestEmail()
            .build()
    }

    single {
        GoogleSignIn.getClient(androidApplication(), get<GoogleSignInOptions>())
    }

    single<GoogleServicesRepository> {
        GoogleServicesRepositoryImpl(
            get(),  // FirebaseAuth
            get(),  // SignInClient
            get(named(SIGN_IN_REQUEST)),
            get(named(SIGN_UP_REQUEST)),
            get()   // FirebaseFirestore
        )
    }

    single<IsFavoriteStateUseCase> { IsFavoriteStateUseCase(get()) }
    single<GetFavoriteCoinsUseCase> { GetFavoriteCoinsUseCase(get(), get()) }
    single<ProvideUserStateUseCase> { ProvideUserStateUseCase(get(), get(), get()) }
    single<SignOutUseCase> { SignOutUseCase(get(), get(), get()) }
    single<CacheUserDataUseCase> { CacheUserDataUseCase(get(), get()) }
    single<DashCoinUseCases> { DashCoinUseCases(get(), get(), get(), get(), get()) }
}
