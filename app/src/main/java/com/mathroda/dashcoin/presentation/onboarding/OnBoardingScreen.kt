package com.mathroda.dashcoin.presentation.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.mathroda.dashcoin.presentation.onboarding.components.CustomOnBoardingButton
import com.mathroda.dashcoin.presentation.onboarding.components.PagerScreen
import com.mathroda.dashcoin.presentation.onboarding.utils.OnBoardingPage
import com.mathroda.dashcoin.presentation.ui.theme.CustomBrightGreen
import com.mathroda.dashcoin.presentation.ui.theme.CustomGreen
import com.mathroda.dashcoin.presentation.ui.theme.LighterGray

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen() {

    val pager = listOf(
        OnBoardingPage.FirstScreen,
        OnBoardingPage.SecondScreen,
        OnBoardingPage.ThirdScreen,
    )
    val state = rememberPagerState()

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(bottom = 32.dp)){
        HorizontalPagerIndicator(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(1f),
            pagerState = state
        )

        HorizontalPager(
            modifier = Modifier.weight(10f),
            count = 3,
            state = state,
            verticalAlignment = Alignment.Top
        ) { position ->
            PagerScreen(onBoardingPage = pager[position], pagerState = state)
        }

        CustomOnBoardingButton(
            modifier = Modifier.weight(1f),
            pagerState = state
        ) {

        }
    }
}
