package com.mathroda.dashcoin.navigation.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.navDeepLink
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.mathroda.SignUpScreen
import com.mathroda.coin_detail.CoinDetailScreen
import com.mathroda.coins_screen.CoinScreen
import com.mathroda.common.navigation.Destinations
import com.mathroda.common.navigation.DestinationsDeepLink
import com.mathroda.common.util.enterTransition
import com.mathroda.common.util.exitTransition
import com.mathroda.common.util.popEnterTransition
import com.mathroda.common.util.popExitTransition
import com.mathroda.dashcoin.navigation.root.Graph
import com.mathroda.favorite_coins.WatchListScreen
import com.mathroda.news_screen.NewsScreen
import com.mathroda.profile_screen.settings.SettingsScreen
import com.mathroda.signin_screen.SignInScreen

@ExperimentalLayoutApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable

fun MainGraph(navController: NavHostController) {

    AnimatedNavHost(
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
            enterTransition = {
                when (initialState.destination.route) {
                    Destinations.CoinDetailScreen.route + "/{coinId}" -> {
                        enterTransition
                    }
                    Destinations.SignIn.route -> {
                        enterTransition
                    }
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Destinations.CoinDetailScreen.route + "/{coinId}" -> {
                        exitTransition
                    }
                    Destinations.SignIn.route -> {
                        exitTransition
                    }
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Destinations.CoinDetailScreen.route + "/{coinId}" -> {
                        popEnterTransition
                    }
                    Destinations.SignIn.route -> {
                        popEnterTransition
                    }
                    else -> null
                }
            }

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
            enterTransition = {
                when (initialState.destination.route) {
                    Destinations.CoinDetailScreen.route + "/{coinId}" -> {
                        enterTransition
                    }
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Destinations.CoinDetailScreen.route + "/{coinId}" -> {
                        exitTransition
                    }
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Destinations.CoinDetailScreen.route + "/{coinId}" -> {
                        popEnterTransition
                    }
                    else -> null
                }
            }
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
            enterTransition = {
                when (initialState.destination.route) {
                    Destinations.CoinsScreen.route -> {
                        enterTransition
                    }
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Destinations.CoinsScreen.route -> {
                        exitTransition
                    }
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    Destinations.CoinsScreen.route -> {
                        popExitTransition
                    }
                    else -> null
                }
            }
        ) {
            CoinDetailScreen(navController = navController)
        }
        composable(
            route = Destinations.SignIn.route,
            enterTransition = {
                when (initialState.destination.route) {
                    Destinations.CoinsScreen.route -> {
                        enterTransition
                    }
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Destinations.CoinsScreen.route -> {
                        exitTransition
                    }
                    else -> null
                }
            },
            popExitTransition = {
                when (initialState.destination.route) {
                    Destinations.CoinsScreen.route -> {
                        popExitTransition
                    }
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Destinations.SignUp.route -> {
                        popEnterTransition
                    }
                    else -> null
                }
            }
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
            enterTransition = {
                when (initialState.destination.route) {
                    Destinations.SignIn.route -> {
                        enterTransition
                    }
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Destinations.SignIn.route -> {
                        exitTransition
                    }
                    else -> null
                }
            },
            popExitTransition = {
                when (initialState.destination.route) {
                    Destinations.SignIn.route -> {
                        popExitTransition
                    }
                    else -> null
                }
            },
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
            com.mathroda.forgot_password.ForgotPasswordScreen(navController = navController)
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