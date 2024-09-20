package com.mathroda.shared.di

import com.mathroda.ResetPasswordViewModel
import com.mathroda.coin_details.CoinDetailViewModel
import com.mathroda.coins.CoinsViewModel
import com.mathroda.favorites.FavoriteCoinsViewModel
import com.mathroda.news.NewsViewModel
import com.mathroda.onboarding.OnBoardingViewModel
import com.mathroda.profile.ProfileViewModel
import com.mathroda.profile.settings.SettingViewModel
import com.mathroda.shared.SharedViewModel
import com.mathroda.signin_screen.SignInViewModel
import com.mathroda.signup.SignUpViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { ResetPasswordViewModel(get()) }
    viewModel { SignInViewModel(get(),get(),get(),get()) }
    viewModel { SignUpViewModel(get(),get()) }
    viewModel { CoinDetailViewModel(get(),get(), get()) }
    viewModelOf(::CoinsViewModel)
    viewModel { FavoriteCoinsViewModel(get(), get(), get()) }
    viewModel { NewsViewModel(get(), get()) }
    viewModel { OnBoardingViewModel(get()) }
    viewModelOf(::ProfileViewModel)
    viewModel { SharedViewModel(get(), get()) }
    viewModel { SettingViewModel(get(), get()) }
}