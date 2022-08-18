package com.mathroda.dashcoin.navigation.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mathroda.dashcoin.navigation.graphs.Graph
import com.mathroda.dashcoin.presentation.signin_screen.SignUpScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTH,
        startDestination = AuthScreen.SignUp.route
    ) {
        composable(route = AuthScreen.SignUp.route) {
            SignUpScreen(
                navigate =  {
                    navController.popBackStack()
                    navController.navigate(Graph.MAIN)
                }
            )
        }
    }
}

sealed class AuthScreen(val route: String) {
    object SignUp: AuthScreen(route = "SIGN_UP")
    object SignIn: AuthScreen(route = "SIGN_IN")
}