package com.mathroda.dashcoin.navigation.main

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.mathroda.dashcoin.core.util.enterTransition
import com.mathroda.dashcoin.core.util.exitTransition
import com.mathroda.dashcoin.core.util.popEnterTransition
import com.mathroda.dashcoin.core.util.popExitTransition
import com.mathroda.dashcoin.navigation.root.Graph
import com.mathroda.dashcoin.presentation.coin_detail.CoinDetailScreen
import com.mathroda.dashcoin.presentation.coins_screen.CoinScreen
import com.mathroda.dashcoin.presentation.forgot_password.ForgotPasswordScreen
import com.mathroda.dashcoin.presentation.news_screen.NewsScreen
import com.mathroda.dashcoin.presentation.signin_screen.SignInScreen
import com.mathroda.dashcoin.presentation.signup_screen.SignUpScreen
import com.mathroda.dashcoin.presentation.watchlist_screen.WatchListScreen

@ExperimentalAnimationApi
@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterialApi
@Composable

fun MainGraph(navController: NavHostController) {
    
    AnimatedNavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = Screens.CoinsScreen.route,
    ) {
        composable(
            route = Screens.CoinsScreen.route,
            enterTransition = {
                when (initialState.destination.route) {
                    Screens.CoinDetailScreen.route + "/{coinId}" -> { enterTransition }
                    Screens.SignIn.route -> { enterTransition }
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Screens.CoinDetailScreen.route + "/{coinId}" -> { exitTransition }
                    Screens.SignIn.route -> { exitTransition }
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Screens.CoinDetailScreen.route + "/{coinId}" -> { popEnterTransition }
                    Screens.SignIn.route -> { popEnterTransition }
                    else -> null
                }
            }

        ){
            CoinScreen(navController = navController)
        }

        composable(
            route = Screens.CoinsWatchList.route,
            enterTransition = {
                when (initialState.destination.route) {
                    Screens.CoinDetailScreen.route + "/{coinId}" -> { enterTransition }
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Screens.CoinDetailScreen.route + "/{coinId}" -> { exitTransition }
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Screens.CoinDetailScreen.route + "/{coinId}" -> { popEnterTransition }
                    else -> null
                }
            }
        ){
            WatchListScreen(navController = navController)
        }

        composable(
            route = Screens.CoinsNews.route
        ){
            NewsScreen()
        }

        composable(
            route = Screens.CoinDetailScreen.route + "/{coinId}",
            enterTransition = {
                when (initialState.destination.route) {
                    Screens.CoinsScreen.route -> { enterTransition }
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Screens.CoinsScreen.route -> { exitTransition }
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    Screens.CoinsScreen.route -> { popExitTransition }
                    else -> null
                }
            }
        ){
            CoinDetailScreen(navController = navController)
        }
        composable(
            route = Screens.SignIn.route,
            enterTransition = {
                when (initialState.destination.route) {
                    Screens.CoinsScreen.route -> { enterTransition }
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Screens.CoinsScreen.route -> { exitTransition }
                    else -> null
                }
            },
            popExitTransition = {
                when (initialState.destination.route) {
                    Screens.CoinsScreen.route -> { popExitTransition }
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Screens.SignUp.route -> { popEnterTransition }
                    else -> null
                }
            }
        ) {
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

        composable(
            route = Screens.SignUp.route,
            enterTransition = {
                when (initialState.destination.route) {
                    Screens.SignIn.route -> { enterTransition }
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Screens.SignIn.route -> { exitTransition }
                    else -> null
                }
            },
            popExitTransition = {
                when (initialState.destination.route) {
                    Screens.SignIn.route -> { popExitTransition }
                    else -> null
                }
            },
        ) {
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