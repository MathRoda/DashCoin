package com.mathroda.dashcoin.presentation.onboarding.utils

import androidx.annotation.DrawableRes
import com.mathroda.dashcoin.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    val title: String,
    val description: String
) {
    object FirstScreen: OnBoardingPage(
        image = R.drawable.ic_onboarding_second,
        title = "Top 100 Coin",
        description = "You will get access to all information's about the top 100 coin in the market"
    )

    object SecondScreen: OnBoardingPage(
        image = R.drawable.ic_onboarding_first,
        title = "Track Cryptocurrencies",
        description = "tracking your favorite coins feature which allows you to get custom notifications about the coin"
    )

    object ThirdScreen: OnBoardingPage(
        image = R.drawable.ic_onboarding_third,
        title = "The Hottest News",
        description = "You have access to the hottest and newest news about the market"
    )
}
