package com.mathroda.dashcoin.ui.screens

import cafe.adriel.voyager.core.registry.screenModule
import com.mathroda.core.destination.DashCoinDestinations

val onboardingScreen = screenModule {
    register<DashCoinDestinations.Onboarding> { OnboardingScreen() }
}

val signInScreen = screenModule {
    register<DashCoinDestinations.SignIn> { SignInScreen() }
}

val signUpScreen = screenModule {
    register<DashCoinDestinations.SignUp> { SignUpScreen() }
}

val forgotPasswordScreen = screenModule {
    register<DashCoinDestinations.ForgotPassword> { ForgotPasswordScreen() }
}

val coinsScreen = screenModule {
    register<DashCoinDestinations.Coins> { CoinsScreen }
}

val coinDetailsScreen = screenModule {
    register<DashCoinDestinations.CoinDetails> { screen -> CoinDetailsScreen(screen.id) }
}

val favoriteCoinsScreen = screenModule {
    register<DashCoinDestinations.FavoriteCoins> { FavoriteCoinsScreen }
}

val newsScreen = screenModule {
    register<DashCoinDestinations.News> { NewsScreen }
}

val settingsScreen = screenModule {
    register<DashCoinDestinations.Settings> { SettingsScreen() }
}