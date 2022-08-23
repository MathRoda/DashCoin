package com.mathroda.dashcoin.navigation.spalsh

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mathroda.dashcoin.navigation.graphs.Graph
import com.mathroda.dashcoin.presentation.splash_screen.AnimatedSplashScreen

fun NavGraphBuilder.splashNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.SPLASH,
        startDestination = SplashScreen.Splash.route
    ) {
        composable(route = SplashScreen.Splash.route) {
            AnimatedSplashScreen(navController = navController)
        }
    }
}

sealed class SplashScreen(val route: String) {
    object Splash: SplashScreen(route = "SPLASH")
}
