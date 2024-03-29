package com.mathroda.dashcoin.navigation.onboarding

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mathroda.dashcoin.navigation.root.Graph

fun NavGraphBuilder.onBoardingNavGraph(navController: NavController) {
    navigation(
        route = Graph.ON_BOARDING,
        startDestination = OnBoardingScreen.Welcome.route
    ) {
        composable(route = OnBoardingScreen.Welcome.route) {
            com.mathroda.OnBoardingScreen(
                popBackStack = {
                    navController.popBackStack()
                },
            )
        }
    }
}

sealed class OnBoardingScreen(val route: String) {
    object Welcome : OnBoardingScreen("welcome_screen")
}