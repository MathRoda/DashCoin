package com.mathroda.datasource.di

import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.datastore.DataStoreRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.datasource.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    @Singleton
    fun providesIsFavoriteStateUseCase(
        dashCoinRepository: DashCoinRepository
    ): IsFavoriteStateUseCase {
        return IsFavoriteStateUseCase(dashCoinRepository)
    }

    @Provides
    @Singleton
    fun providesGetAllFavoriteCoinsUseCase(
        dashCoinRepository: DashCoinRepository,
        firebaseRepository: FirebaseRepository
    ): GetFavoriteCoinsUseCase {
        return GetFavoriteCoinsUseCase(dashCoinRepository, firebaseRepository)
    }

    @Provides
    @Singleton
    fun providesUserStateUseCase(
        dashCoinRepository: DashCoinRepository,
        firebaseRepository: FirebaseRepository,
        dataStoreRepository: DataStoreRepository
    ): ProvideUserStateUseCase {
        return ProvideUserStateUseCase(
            firebaseRepository, dataStoreRepository, dashCoinRepository
        )
    }

    @Provides
    @Singleton
    fun providesSignOutUseCase(
        dashCoinRepository: DashCoinRepository,
        firebaseRepository: FirebaseRepository,
        dataStoreRepository: DataStoreRepository
    ): SignOutUseCase {
        return SignOutUseCase(
            firebaseRepository, dashCoinRepository, dataStoreRepository
        )
    }

    @Provides
    @Singleton
    fun providesCacheUserDataUseCase(
        dashCoinRepository: DashCoinRepository,
        firebaseRepository: FirebaseRepository
    ): CacheUserDataUseCase {
        return CacheUserDataUseCase(
            dashCoinRepository, firebaseRepository
        )
    }

    @Provides
    @Singleton
    fun providesDashCoinUseCases(
        isFavorite: IsFavoriteStateUseCase,
        getAllFavoriteCoins: GetFavoriteCoinsUseCase,
        userStateProvider: ProvideUserStateUseCase,
        signOutUseCase: SignOutUseCase,
        cacheUserData: CacheUserDataUseCase
    ): DashCoinUseCases {
        return DashCoinUseCases(
            isFavorite,
            getAllFavoriteCoins,
            userStateProvider,
            signOutUseCase,
            cacheUserData
        )
    }
}