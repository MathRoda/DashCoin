package com.mathroda.dashcoin.navigation.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.mathroda.SignUpScreen
import com.mathroda.coin_detail.CoinDetailScreen
import com.mathroda.coins_screen.CoinScreen
import com.mathroda.common.navigation.Destinations
import com.mathroda.common.navigation.DestinationsDeepLink
import com.mathroda.dashcoin.navigation.root.Graph
import com.mathroda.favorite_coins.WatchListScreen
import com.mathroda.forgot_password.ForgotPasswordScreen
import com.mathroda.news_screen.NewsScreen
import com.mathroda.profile_screen.settings.SettingsScreen
import com.mathroda.signin_screen.SignInScreen

@ExperimentalLayoutApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable

fun MainGraph(navController: NavHostController, paddingValues: PaddingValues) {

    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = Destinations.CoinsScreen.route,
    ) {
        composable(
            route = Destinations.CoinsScreen.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = DestinationsDeepLink.HomePattern
                }
            ),
        ) {
            CoinScreen(navController = navController)
        }

        composable(
            route = Destinations.FavoriteCoinsScreen.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = DestinationsDeepLink.FavoriteCoinsPattern
                }
            ),
        ) {
            WatchListScreen(navController = navController)
        }

        composable(
            route = Destinations.CoinsNews.route
        ) {
            NewsScreen()
        }

        composable(
            route = Destinations.CoinDetailScreen.route + "/{coinId}",
        ) {
            CoinDetailScreen(navController = navController)
        }
        composable(
            route = Destinations.SignIn.route,
        ) {
            SignInScreen(
                navigateToCoinsScreen = {
                    navController.popBackStack()
                    navController.navigate(Destinations.CoinsScreen.route)
                },
                navigateToSignUpScreen = {
                    navController.navigate(Destinations.SignUp.route)
                },
                popBackStack = {
                    navController.popBackStack(Destinations.SignUp.route, false)
                },
                navigateToForgotPassword = {
                    navController.navigate(Destinations.ForgotPassword.route)
                }
            )
        }

        composable(
            route = Destinations.SignUp.route,
        ) {
            SignUpScreen(
                navigateToSignInScreen = {
                    navController.navigate(Destinations.SignIn.route)
                },
                popBackStack = {
                    navController.popBackStack(Destinations.SignIn.route, false)
                }
            )
        }

        composable(route = Destinations.ForgotPassword.route) {
            ForgotPasswordScreen(navController = navController)
        }

        composable(
            route = Destinations.Settings.route
        ) {
            SettingsScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
    }

}