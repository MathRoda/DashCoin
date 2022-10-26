package com.mathroda.dashcoin.presentation.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.mathroda.dashcoin.presentation.onboarding.components.CustomOnBoardingButton
import com.mathroda.dashcoin.presentation.onboarding.components.PagerScreen
import com.mathroda.dashcoin.presentation.onboarding.utils.OnBoardingPage
import com.mathroda.dashcoin.presentation.onboarding.viewmodel.OnBoardingViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(
    viewModel: OnBoardingViewModel = hiltViewModel(),
    popBackStack: () -> Unit,
    toAuthScreen: () -> Unit,
    toMainScreen: () -> Unit
) {
    val isOnBoardingCompleted = viewModel.isOnBoardingCompleted.collectAsState()
    val signAnonymouslyState = viewModel.signAnonymously.collectAsState()
    val isAnonymous = signAnonymouslyState.value.success != null

    val pager = viewModel.pager
    val state = rememberPagerState()

    /*if (isOnBoardingCompleted.value) {
        LaunchedEffect(Unit) {
            if (isAnonymous) {
                toMainScreen()
            } else {
                toAuthScreen()
            }
        }
    }*/

    if (isAnonymous) {
        LaunchedEffect(Unit) {
            toMainScreen
        }
    }

    if (!isOnBoardingCompleted.value) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp)
        ) {
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
                viewModel.saveOnBoardingState(completed = true)
                popBackStack()
                viewModel.signAnonymously()
            }
        }
    }


}
