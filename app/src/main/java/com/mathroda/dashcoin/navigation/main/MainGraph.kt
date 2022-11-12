package com.mathroda.dashcoin.navigation.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.mathroda.coins_screen.CoinScreen
import com.mathroda.common.navigation.Screens
import com.mathroda.common.util.enterTransition
import com.mathroda.common.util.exitTransition
import com.mathroda.common.util.popEnterTransition
import com.mathroda.common.util.popExitTransition
import com.mathroda.dashcoin.navigation.root.Graph
import com.mathroda.favorite_coins.WatchListScreen
import com.mathroda.news_screen.NewsScreen

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
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
                    Screens.CoinDetailScreen.route + "/{coinId}" -> {
                        enterTransition
                    }
                    Screens.SignIn.route -> {
                        enterTransition
                    }
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Screens.CoinDetailScreen.route + "/{coinId}" -> {
                        exitTransition
                    }
                    Screens.SignIn.route -> {
                        exitTransition
                    }
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Screens.CoinDetailScreen.route + "/{coinId}" -> {
                        popEnterTransition
                    }
                    Screens.SignIn.route -> {
                        popEnterTransition
                    }
                    else -> null
                }
            }

        ) {
            CoinScreen(navController = navController)
        }

        composable(
            route = Screens.CoinsWatchList.route,
            enterTransition = {
                when (initialState.destination.route) {
                    Screens.CoinDetailScreen.route + "/{coinId}" -> {
                        enterTransition
                    }
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Screens.CoinDetailScreen.route + "/{coinId}" -> {
                        exitTransition
                    }
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Screens.CoinDetailScreen.route + "/{coinId}" -> {
                        popEnterTransition
                    }
                    else -> null
                }
            }
        ) {
            WatchListScreen(navController = navController)
        }

        composable(
            route = Screens.CoinsNews.route
        ) {
            NewsScreen()
        }

        composable(
            route = Screens.CoinDetailScreen.route + "/{coinId}",
            enterTransition = {
                when (initialState.destination.route) {
                    Screens.CoinsScreen.route -> {
                        enterTransition
                    }
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Screens.CoinsScreen.route -> {
                        exitTransition
                    }
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    Screens.CoinsScreen.route -> {
                        popExitTransition
                    }
                    else -> null
                }
            }
        ) {
            com.mathroda.coin_detail.CoinDetailScreen(navController = navController)
        }
        composable(
            route = Screens.SignIn.route,
            enterTransition = {
                when (initialState.destination.route) {
                    Screens.CoinsScreen.route -> {
                        enterTransition
                    }
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Screens.CoinsScreen.route -> {
                        exitTransition
                    }
                    else -> null
                }
            },
            popExitTransition = {
                when (initialState.destination.route) {
                    Screens.CoinsScreen.route -> {
                        popExitTransition
                    }
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Screens.SignUp.route -> {
                        popEnterTransition
                    }
                    else -> null
                }
            }
        ) {
            com.mathroda.signin_screen.SignInScreen(
                navigateToCoinsScreen = {
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
                    Screens.SignIn.route -> {
                        enterTransition
                    }
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Screens.SignIn.route -> {
                        exitTransition
                    }
                    else -> null
                }
            },
            popExitTransition = {
                when (initialState.destination.route) {
                    Screens.SignIn.route -> {
                        popExitTransition
                    }
                    else -> null
                }
            },
        ) {
            com.mathroda.SignUpScreen(
                navigateToSignInScreen = {
                    navController.navigate(Screens.SignIn.route)
                },
                popBackStack = {
                    navController.popBackStack(Screens.SignIn.route, false)
                }
            )
        }

        composable(route = Screens.ForgotPassword.route) {
            com.mathroda.forgot_password.ForgotPasswordScreen(navController = navController)
        }
    }

}