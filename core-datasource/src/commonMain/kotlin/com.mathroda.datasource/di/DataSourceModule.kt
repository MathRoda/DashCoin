package com.mathroda.datasource.di

import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.core.DashCoinRepositoryImpl
import com.mathroda.datasource.datastore.DataStoreRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.datasource.firebase.FirebaseRepositoryImpl
import com.mathroda.datasource.usecases.CacheUserDataUseCase
import com.mathroda.datasource.usecases.DashCoinUseCases
import com.mathroda.datasource.usecases.GetFavoriteCoinsUseCase
import com.mathroda.datasource.usecases.IsFavoriteStateUseCase
import com.mathroda.datasource.usecases.ProvideUserStateUseCase
import com.mathroda.datasource.usecases.SignOutUseCase
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.storage.storage
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformModule() : Module
val dataSourceModule = module {
    includes(platformModule())
    single<DashCoinRepository> {
        DashCoinRepositoryImpl(
            api = get(),
            favoriteCoinsDao = get(),
            userDao = get(),
        )
    }

    single { Firebase.auth }
    single { Firebase.firestore }
    single { Firebase.storage }
    single<FirebaseRepository> {
        FirebaseRepositoryImpl(
            firebaseAuth = get(),
            fireStore = get(),
            firebaseStorage = get()
        )
    }

    single { DataStoreRepository(get()) }

    single<IsFavoriteStateUseCase> { IsFavoriteStateUseCase(get()) }
    single<GetFavoriteCoinsUseCase> { GetFavoriteCoinsUseCase(get(), get()) }
    single<ProvideUserStateUseCase> { ProvideUserStateUseCase(get(), get(), get()) }
    single<SignOutUseCase> { SignOutUseCase(get(), get(), get()) }
    single<CacheUserDataUseCase> { CacheUserDataUseCase(get(), get()) }
    single<DashCoinUseCases> { DashCoinUseCases(get(), get(), get(), get(), get()) }
}
