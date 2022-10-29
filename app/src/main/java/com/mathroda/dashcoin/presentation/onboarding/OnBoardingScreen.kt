package com.mathroda.dashcoin.presentation.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.mathroda.dashcoin.presentation.onboarding.components.*
import com.mathroda.dashcoin.presentation.onboarding.utils.OnBoardingPage
import com.mathroda.dashcoin.presentation.onboarding.viewmodel.OnBoardingViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(
    viewModel: OnBoardingViewModel = hiltViewModel(),
    popBackStack: () -> Unit,
) {
    val pager = viewModel.pager
    val state = rememberPagerState()
    val scope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp)
        ) {

            OnBoardingTopSection(
                modifier = Modifier.weight(1f),
                size = pager.size,
                index = state.currentPage,
                onBackClick = {
                    if (state.currentPage + 1 > 1) scope.launch {
                        state.scrollToPage(state.currentPage - 1)
                    }
                },
                onSkipClick = {
                    if (state.currentPage + 1 < pager.size) scope.launch {
                        state.scrollToPage(pager.size - 1)
                    }
                }
            )

            HorizontalPager(
                modifier = Modifier.weight(10f),
                count = 3,
                state = state,
                verticalAlignment = Alignment.Top
            ) { position ->
                PagerScreen(onBoardingPage = pager[position])
            }


                AnimatedVisibility(
                    visible = state.currentPage != 2
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Bottom,
                        horizontalArrangement = Arrangement.End
                    ) {
                        CustomBottomSection() {
                            if (state.currentPage + 1 < pager.size) scope.launch {
                                state.scrollToPage(state.currentPage + 1)
                            }
                        }
                   }
               }

            Box {
                CustomOnBoardingButton(
                    pagerState = state
                ) {
                    viewModel.saveOnBoardingState(completed = true)
                    popBackStack()
                }
            }
        }


}
