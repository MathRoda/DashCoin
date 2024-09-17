package com.mathroda.shared.navigation.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mathroda.BasicForgotPasswordScreen
import com.mathroda.ResetPasswordViewModel
import com.mathroda.coin_details.BasicCoinDetailScreen
import com.mathroda.coin_details.CoinDetailViewModel
import com.mathroda.coins.BasicCoinScreen
import com.mathroda.coins.CoinsViewModel
import com.mathroda.favorites.FavoriteCoinsViewModel
import com.mathroda.favorites.WatchListScreen
import com.mathroda.news.NewsScreen
import com.mathroda.news.NewsViewModel
import com.mathroda.onboarding.BasicOnboarding
import com.mathroda.onboarding.OnBoardingViewModel
import com.mathroda.profile.ProfileViewModel
import com.mathroda.profile.settings.SettingViewModel
import com.mathroda.profile.settings.SettingsScreen
import com.mathroda.shared.destination.Destinations
import com.mathroda.shared.navigation.toCoinDetails
import com.mathroda.shared.navigation.toForgotPassword
import com.mathroda.shared.navigation.toSettings
import com.mathroda.shared.navigation.toSignIn
import com.mathroda.shared.navigation.toSignUp
import com.mathroda.signin_screen.BasicSignIn
import com.mathroda.signin_screen.SignInViewModel
import com.mathroda.signup.BasicSignUpScreen
import com.mathroda.signup.SignUpViewModel
import org.koin.compose.viewmodel.koinViewModel

@ExperimentalLayoutApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable

fun MainGraph(
    navController: NavHostController,
    startDestinations: Destinations
) {

    NavHost(
        navController = navController,
        startDestination = startDestinations.route,
    ) {
        composable(route = Destinations.Onboarding.route) {
            val viewModel: OnBoardingViewModel = koinViewModel()
            BasicOnboarding(
                viewModel = viewModel,
                popBackStack = {
                    navController.popBackStack()
                },
            )
        }

        composable(
            route = Destinations.CoinsScreen.route
        ) {
            val viewModel = koinViewModel<CoinsViewModel>()
            val profileViewModel = koinViewModel<ProfileViewModel>()
            BasicCoinScreen(
                viewModel = viewModel,
                profileViewModel = profileViewModel,
                navigateToSignIn = navController::toSignIn ,
                navigateToCoinDetails = navController::toCoinDetails ,
                navigateToSettings = navController::toSettings
            )
        }

        composable(
            route = Destinations.FavoriteCoinsScreen.route
        ) {
            val viewModel: FavoriteCoinsViewModel = koinViewModel()
            WatchListScreen(
                viewModel = viewModel,
                navigateToCoinDetails = navController::toCoinDetails,
                navigateToSignIn = navController::toSignIn
            )
        }

        composable(
            route = Destinations.CoinsNews.route
        ) {
            val viewModel: NewsViewModel = koinViewModel()
            NewsScreen(
                viewModel = viewModel
            )
        }

        composable(
            route = Destinations.CoinDetailScreen.route + "/{coinId}",
            arguments = listOf(
                navArgument("coinId") {
                    nullable = true
                    defaultValue = null
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val viewModel: CoinDetailViewModel = koinViewModel()
            BasicCoinDetailScreen(
                viewModel = viewModel,
                coinId = backStackEntry.arguments?.getString("coinId") ?: "",
                popBackStack = navController::popBackStack
            )
        }
        composable(
            route = Destinations.SignIn.route
        ) {
            val viewModel: SignInViewModel = koinViewModel()
            BasicSignIn(
                viewModel = viewModel,
                navigateToSignUpScreen = navController::toSignUp,
                navigateToForgotPassword = navController::toForgotPassword,
                popBackStack = navController::popBackStack
            )
        }

        composable(
            route = Destinations.SignUp.route
        ) {
            val viewModel: SignUpViewModel = koinViewModel()
            BasicSignUpScreen(
                viewModel = viewModel,
                navigateBack = navController::popBackStack
            )
        }

        composable(route = Destinations.ForgotPassword.route) {
            val viewModel: ResetPasswordViewModel = koinViewModel()
           BasicForgotPasswordScreen(
               viewModel = viewModel,
               navigateBack = navController::popBackStack
           )
        }

        composable(
            route = Destinations.Settings.route
        ) {
            val viewModel: SettingViewModel = koinViewModel()
            SettingsScreen(
                viewModel = viewModel,
                navigateBack = navController::popBackStack
            )
        }
    }

}