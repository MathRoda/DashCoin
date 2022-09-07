package com.mathroda.dashcoin.navigation.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mathroda.dashcoin.navigation.root.Graph
import com.mathroda.dashcoin.presentation.signin_screen.SignInScreen
import com.mathroda.dashcoin.presentation.signup_screen.SignUpScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTH,
        startDestination = AuthScreen.SignIn.route
    ) {
        composable(route = AuthScreen.SignIn.route) {
            SignInScreen(
                navigateToCoinsScreen =  {
                    navController.popBackStack()
                    navController.navigate(Graph.MAIN)
                },
                navigateToSignUpScreen = {
                    navController.navigate(AuthScreen.SignUp.route)
                },
                popBackStack = {
                    navController.popBackStack(AuthScreen.SignUp.route, false)
                }
            )
        }

        composable(route = AuthScreen.SignUp.route) {
            SignUpScreen(
                navigateToSignInScreen =  {
                    navController.popBackStack()
                    navController.navigate(AuthScreen.SignIn.route)
                }
            )
        }
    }
}

sealed class AuthScreen(val route: String) {
    object SignUp: AuthScreen(route = "SIGN_UP")
    object SignIn: AuthScreen(route = "SIGN_IN")
}