package com.mathroda.dashcoin.navigation.onboarding

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mathroda.dashcoin.navigation.root.Graph
import com.mathroda.dashcoin.presentation.onboarding.OnBoardingScreen

fun NavGraphBuilder.onBoardingNavGraph(navController: NavController) {
    navigation(
        route = Graph.ON_BOARDING,
        startDestination = OnBoardingScreen.Welcome.route
    ) {
        composable(route = OnBoardingScreen.Welcome.route) {
            OnBoardingScreen(
                popBackStack = {
                    navController.popBackStack()
                },
                toAuthScreen = {
                    navController.navigate(Graph.AUTH)
                },
                toMainScreen = {
                    navController.navigate(Graph.MAIN)
                }
            )
        }
    }
}

sealed class OnBoardingScreen(val route: String) {
    object Welcome: OnBoardingScreen("welcome_screen")
}