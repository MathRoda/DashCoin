package com.mathroda.shared.navigation.main

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
            val viewModel: CoinsViewModel = koinViewModel()
            val profileViewModel: ProfileViewModel = koinViewModel()
            BasicCoinScreen(
                viewModel = viewModel,
                profileViewModel = profileViewModel,
                navigateToSignIn ={} ,
                navigateToCoinDetails = {} ,
                navigateToSettings = {}
            )
        }

        composable(
            route = Destinations.FavoriteCoinsScreen.route,
        ) {
            val viewModel: FavoriteCoinsViewModel = koinViewModel()
            WatchListScreen(
                viewModel = viewModel,
                navigateToCoinDetails = {},
                navigateToSignIn = {}
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
            route = Destinations.CoinDetailScreen.route + "/{coinId}"
        ) {
            val viewModel: CoinDetailViewModel = koinViewModel()
            BasicCoinDetailScreen(
                viewModel = viewModel,
                coinId = "",
                popBackStack = {}
            )
        }
        composable(
            route = Destinations.SignIn.route
        ) {
            val viewModel: SignInViewModel = koinViewModel()
            BasicSignIn(
                viewModel = viewModel,
                navigateToSignUpScreen = {},
                navigateToForgotPassword = {},
                popBackStack = {}
            )
        }

        composable(
            route = Destinations.SignUp.route
        ) {
            val viewModel: SignUpViewModel = koinViewModel()
            BasicSignUpScreen(
                viewModel = viewModel,
                navigateBack = {}
            )
        }

        composable(route = Destinations.ForgotPassword.route) {
            val viewModel: ResetPasswordViewModel = koinViewModel()
           BasicForgotPasswordScreen(
               viewModel = viewModel,
               navigateBack = {}
           )
        }

        composable(
            route = Destinations.Settings.route
        ) {
            val viewModel: SettingViewModel = koinViewModel()
            SettingsScreen(
                viewModel = viewModel,
                navigateBack = {}
            )
        }
    }

}