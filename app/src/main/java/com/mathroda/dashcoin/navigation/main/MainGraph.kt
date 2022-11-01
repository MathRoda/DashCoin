package com.mathroda.dashcoin.navigation.main

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mathroda.dashcoin.navigation.root.Graph
import com.mathroda.dashcoin.presentation.coin_detail.CoinDetailScreen
import com.mathroda.dashcoin.presentation.coins_screen.CoinScreen
import com.mathroda.dashcoin.presentation.forgot_password.ForgotPasswordScreen
import com.mathroda.dashcoin.presentation.news_screen.NewsScreen
import com.mathroda.dashcoin.presentation.signin_screen.SignInScreen
import com.mathroda.dashcoin.presentation.signup_screen.SignUpScreen
import com.mathroda.dashcoin.presentation.watchlist_screen.WatchListScreen
import com.mathroda.dashcoin.presentation.watchlist_screen.authed_users.WatchListAuthedUsers

@ExperimentalMaterialApi
@Composable
fun MainGraph(navController: NavHostController) {
    
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = Screens.CoinsScreen.route
    ) {
        composable(
            route = Screens.CoinsScreen.route
        ){
            CoinScreen(navController = navController)
        }

        composable(
            route = Screens.CoinsWatchList.route
        ){
            WatchListScreen(navController = navController)
        }

        composable(
            route = Screens.CoinsNews.route
        ){
            NewsScreen()
        }

        composable(
            route = Screens.CoinDetailScreen.route + "/{coinId}"
        ){
            CoinDetailScreen(navController = navController)
        }
        composable(route = Screens.SignIn.route) {
            SignInScreen(
                navigateToCoinsScreen =  {
                    navController.popBackStack()
                    navController.navigate(Screens.CoinsScreen.route)
                },
                navigateToSignUpScreen = {
                    navController.navigate(Screens.SignUp.route)
                },
                popBackStack = {
                    navController.popBackStack(Screens.SignUp.route, false)
                },
                navigateToForgotPassword = {
                    navController.navigate(Screens.ForgotPassword.route)
                }
            )
        }

        composable(route = Screens.SignUp.route) {
            SignUpScreen(
                navigateToSignInScreen =  {
                    navController.navigate(Screens.SignIn.route)
                },
                popBackStack = {
                    navController.popBackStack(Screens.SignIn.route, false)
                }
            )
        }

        composable(route = Screens.ForgotPassword.route){
            ForgotPasswordScreen(navController = navController)
        }
    }

}