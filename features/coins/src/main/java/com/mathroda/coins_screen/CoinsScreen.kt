package com.mathroda.coins_screen

import android.view.MotionEvent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
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
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mathroda.coins_screen.components.CoinsItem
import com.mathroda.coins_screen.components.CoinsScreenState
import com.mathroda.coins_screen.components.CoinsScreenTopBar
import com.mathroda.coins_screen.components.ScrollButton
import com.mathroda.coins_screen.components.SearchBar
import com.mathroda.common.components.InfiniteListHandler
import com.mathroda.common.theme.DarkGray
import com.mathroda.common.theme.LightGray
import com.mathroda.profile_screen.ProfileViewModel
import com.mathroda.profile_screen.drawer.DrawerNavigation
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

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
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                    onRefresh = {
                        if (isBeingSearched.isEmpty()) {
                            viewModel.refresh()
                        }
                    }
                ) {

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

                    InfiniteListHandler(lazyListState = lazyListState) {
                        viewModel.getCoinsPaginated()
                    }
                }

            }

            CoinsScreenState(viewModel = viewModel)
        }

        ScrollButton(lazyListState = lazyListState)
    }

}

