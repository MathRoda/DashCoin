package com.mathroda.coins_screen

import android.view.MotionEvent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mathroda.coins_screen.components.CoinsScreenState
import com.mathroda.coins_screen.components.ScrollButton
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
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
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
                                        searchCoin.value = TextFieldValue("")
                                    }
                                    MotionEvent.ACTION_UP -> {
                                        focusManger.clearFocus()
                                        searchCoin.value = TextFieldValue("")
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

            CoinsScreenState()
        }

        ScrollButton(lazyListState = lazyListState)
    }

}
