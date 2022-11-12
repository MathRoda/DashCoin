package com.mathroda.coins_screen

import android.view.MotionEvent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mathroda.coins_screen.components.isScrollingUp
import com.mathroda.common.navigation.Screens
import com.mathroda.profile_screen.DrawerNavigation
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun CoinScreen(
    viewModel: CoinsViewModel = hiltViewModel(),
    navController: NavController
) {
    val state = viewModel.state.collectAsState()
    val isRefreshing by viewModel.isRefresh.collectAsState()
    val searchCoin = remember { mutableStateOf(TextFieldValue("")) }
    val lottieComp by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(com.mathroda.common.R.raw.loading_main))
    val lottieProgress by animateLottieCompositionAsState(
        composition = lottieComp,
        iterations = LottieConstants.IterateForever,
    )
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val isUserExists = viewModel.isCurrentUserExist.collectAsState(initial = false).value
    val focusManger = LocalFocusManager.current
    val lazyListState = rememberLazyListState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            com.mathroda.coins_screen.components.CoinsScreenTopBar(
                title = "Live Prices",
                onNavigationDrawerClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            )
        },
        drawerContent = {
            DrawerNavigation(
                isUserExists = isUserExists,
                navController = navController
            )
        },
        drawerBackgroundColor = com.mathroda.common.theme.DarkGray,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerScrimColor = com.mathroda.common.theme.LightGray

    ) {
        Box(
            modifier = Modifier
                .background(com.mathroda.common.theme.DarkGray)
                .fillMaxSize()
        ) {
            Column {
                com.mathroda.coins_screen.components.SearchBar(
                    hint = "Search...",
                    modifier = Modifier
                        .fillMaxWidth(),
                    state = searchCoin
                )
                val isBeingSearched = searchCoin.value.text
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                    onRefresh = { viewModel.refresh() }) {

                    LazyColumn(
                        modifier = Modifier
                            .pointerInteropFilter {
                                when (it.action) {
                                    MotionEvent.ACTION_DOWN -> {
                                        focusManger.clearFocus()
                                    }
                                    MotionEvent.ACTION_UP -> {
                                        focusManger.clearFocus()
                                    }
                                }
                                false
                            },
                        state = lazyListState
                    ) {
                        items(items = state.value.coins.filter {
                            it.name.contains(isBeingSearched, ignoreCase = true) ||
                                    it.id.contains(isBeingSearched, ignoreCase = true) ||
                                    it.symbol.contains(isBeingSearched, ignoreCase = true)
                        }, key = { it.id }) { coins ->
                            com.mathroda.coins_screen.components.CoinsItem(
                                coins = coins,
                                onItemClick = {
                                    navController.navigate(Screens.CoinDetailScreen.route + "/${coins.id}")
                                }
                            )
                        }
                    }
                }

            }


            if (state.value.isLoading) {

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    LottieAnimation(
                        composition = lottieComp,
                        progress = { lottieProgress },
                    )
                }
            }

            if (state.value.error.isNotEmpty()) {
                Text(
                    text = state.value.error,
                    color = MaterialTheme.colors.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.Center)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {
            val firstVisibleItem = lazyListState.firstVisibleItemIndex
            val isScrollingUp = lazyListState.isScrollingUp()

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                com.mathroda.coins_screen.components.FloatingScrollButton(
                    modifier = Modifier
                        .padding(bottom = 64.dp, end = 16.dp),
                    visibility = if (firstVisibleItem <= 4) false else isScrollingUp
                ) {
                    scope.launch {
                        /**
                         * Scroll to first item in the list
                         */
                        lazyListState.animateScrollToItem(0)
                    }
                }

            }
        }
    }

}

