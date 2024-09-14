package com.mathroda.coins

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.mathroda.coins.components.CoinsItem
import com.mathroda.coins.components.CoinsScreenState
import com.mathroda.coins.components.CoinsScreenTopBar
import com.mathroda.coins.components.ScrollButton
import com.mathroda.coins.components.SearchBar
import com.mathroda.common.components.InfiniteListHandler
import com.mathroda.common.theme.DarkGray
import com.mathroda.common.theme.LightGray
import com.mathroda.profile.ProfileViewModel
import com.mathroda.profile.drawer.DrawerNavigation
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun BasicCoinScreen(
    viewModel: CoinsViewModel,
    profileViewModel: ProfileViewModel,
    navigateToSettings: () -> Unit,
    navigateToSignIn: () -> Unit,
    navigateToCoinDetails: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val isRefreshing by viewModel.isRefresh.collectAsState()
    val paginationState by viewModel.paginationState.collectAsState()
    val searchCoin = remember { mutableStateOf(TextFieldValue("")) }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val focusManger = LocalFocusManager.current
    val lazyListState = rememberLazyListState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = viewModel::refresh
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CoinsScreenTopBar(
                title = "Live Prices",
                onNavigationDrawerClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                        focusManger.clearFocus()
                    }
                }
            )
        },
        drawerContent = {
            DrawerNavigation(
                viewModel = profileViewModel,
                navigateToSettings = navigateToSettings,
                closeDrawer = {
                    scope.launch { scaffoldState.drawerState.close() }
                },
                navigateToSignIn = {
                    navigateToSignIn()
                    scope.launch { scaffoldState.drawerState.close() }
                }
            )
        },
        drawerBackgroundColor = DarkGray,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerScrimColor = LightGray

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .background(DarkGray)
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column {
                SearchBar(
                    hint = if (searchCoin.value.text.isEmpty()) "Search..." else "",
                    modifier = Modifier.fillMaxWidth(),
                    state = searchCoin
                )
                val isBeingSearched = searchCoin.value.text
                Box(
                   modifier = Modifier
                       .fillMaxWidth()
                       .pullRefresh(pullRefreshState)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .pointerInput(Unit){
                                detectTapGestures(
                                    onPress = {
                                        focusManger.clearFocus()
                                    }
                                )
                            },
                        state = lazyListState
                    ) {
                        items(
                            items = state.coins.filter {
                            it.name.contains(isBeingSearched, ignoreCase = true) ||
                                    it.id.contains(isBeingSearched, ignoreCase = true) ||
                                    it.symbol.contains(isBeingSearched, ignoreCase = true)
                        }, key = { it.uniqueId }) { coins ->
                            CoinsItem(
                                coins = coins,
                                onItemClick = {
                                    navigateToCoinDetails(coins.id)
                                }
                            )
                        }
                        item {
                            if (paginationState.isLoading) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }

                    PullRefreshIndicator(
                        refreshing = viewModel.state.value.isLoading,
                        state = pullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
                InfiniteListHandler(lazyListState = lazyListState) {
                    viewModel.getCoinsPaginated()
                }

            }

            CoinsScreenState(viewModel = viewModel)
        }

        ScrollButton(lazyListState = lazyListState)
    }

}

