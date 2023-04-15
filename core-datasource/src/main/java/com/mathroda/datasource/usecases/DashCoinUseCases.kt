package com.mathroda.datasource.usecases

data class DashCoinUseCases(
    val isFavoriteState: IsFavoriteStateUseCase,
    val getAllFavoriteCoins: GetFavoriteCoinsUseCase,
    val userStateProvider: ProvideUserStateUseCase,
    val signOut: SignOutUseCase
)