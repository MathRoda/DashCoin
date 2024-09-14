@file:OptIn(ExperimentalFoundationApi::class)

package com.mathroda.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mathroda.onboarding.components.CustomBottomSection
import com.mathroda.onboarding.components.CustomOnBoardingButton
import com.mathroda.onboarding.components.OnBoardingTopSection
import com.mathroda.onboarding.components.PagerScreen
import kotlinx.coroutines.launch


@Composable
fun BasicOnboarding(
    viewModel: OnBoardingViewModel,
    popBackStack: () -> Unit,
) {
    val pager = viewModel.pager
    val state = rememberPagerState(
        pageCount = { 3 }
    )
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
