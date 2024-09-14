package com.mathroda.core.destination

import cafe.adriel.voyager.core.registry.ScreenProvider

typealias Destination = ScreenProvider

sealed interface DashCoinDestinations: Destination {
    data object Onboarding: DashCoinDestinations
    data object SignIn: DashCoinDestinations
    data object SignUp: DashCoinDestinations
    data object ForgotPassword: DashCoinDestinations
    data object Coins: DashCoinDestinations
    data class CoinDetails(val id: String): DashCoinDestinations
    data object FavoriteCoins: DashCoinDestinations
    data object News: DashCoinDestinations
    data object Settings: DashCoinDestinations
}