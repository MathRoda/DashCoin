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
        image = R.drawable.ic_onboarding_first,
        title = "Track Crypto",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )

    object SecondScreen: OnBoardingPage(
        image = R.drawable.ic_onboarding_second,
        title = "Top 100 Coin",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )

    object ThirdScreen: OnBoardingPage(
        image = R.drawable.ic_onboarding_third,
        title = "Hottest News",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )
}
