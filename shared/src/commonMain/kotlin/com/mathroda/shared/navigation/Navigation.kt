package com.mathroda.shared.navigation

import androidx.navigation.NavController
import com.mathroda.shared.destination.Destinations

fun NavController.toSignIn() = navigate(Destinations.SignIn.route)
fun NavController.toSignUp() = navigate(Destinations.SignUp.route)
fun NavController.toForgotPassword() = navigate(Destinations.ForgotPassword.route)

fun NavController.toCoinDetails(coinId: String) = navigate(Destinations.CoinDetailScreen.route + "/${coinId}")
fun NavController.toSettings() = navigate(Destinations.Settings.route)
fun NavController.toOnboarding() = navigate(Destinations.Onboarding.route) {
    popBackStack()
    launchSingleTop = true
}

fun NavController.popToCoinsScreen() = navigate(Destinations.CoinsScreen.route) {
    popBackStack()
    launchSingleTop = true
}