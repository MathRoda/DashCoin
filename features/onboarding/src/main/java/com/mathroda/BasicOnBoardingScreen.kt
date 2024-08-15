@file:OptIn(ExperimentalPagerApi::class)

package com.mathroda

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.mathroda.components.CustomBottomSection
import com.mathroda.components.CustomOnBoardingButton
import com.mathroda.components.OnBoardingTopSection
import com.mathroda.components.PagerScreen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun BasicOnboarding(
    popBackStack: () -> Unit,
) {
    val viewModel: OnBoardingViewModel = koinViewModel()
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
            },
            isSkipVisible = state.currentPage != 2
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
                CustomBottomSection {
                    if (state.currentPage + 1 < pager.size) scope.launch {
                        state.scrollToPage(state.currentPage + 1)
                    }
                }
            }
        }

        CustomOnBoardingButton(
            pagerState = state
        ) {
            viewModel.saveOnBoardingState(completed = true)
            popBackStack()
        }

    }


}
