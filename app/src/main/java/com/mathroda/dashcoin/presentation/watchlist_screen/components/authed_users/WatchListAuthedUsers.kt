package com.mathroda.dashcoin.presentation.watchlist_screen.components.authed_users

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mathroda.dashcoin.navigation.main.Screens
import com.mathroda.dashcoin.presentation.coin_detail.components.Chart
import com.mathroda.dashcoin.presentation.coin_detail.viewmodel.CoinViewModel
import com.mathroda.dashcoin.presentation.ui.common.CommonTopBar
import com.mathroda.dashcoin.presentation.ui.theme.CustomGreen
import com.mathroda.dashcoin.presentation.ui.theme.DarkGray
import com.mathroda.dashcoin.presentation.ui.theme.LighterGray
import com.mathroda.dashcoin.presentation.watchlist_screen.viewmodel.WatchListViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun WatchListAuthedUsers(
    watchListViewModel: WatchListViewModel = hiltViewModel(),
    coinViewModel: CoinViewModel = hiltViewModel(),
    navController: NavController
) {
    val watchListState = watchListViewModel.state.collectAsState()
    val isRefresh by watchListViewModel.isRefresh.collectAsState()
    val marketState = coinViewModel.marketStatus.value

    Box(
        modifier = Modifier
            .background(DarkGray)
            .fillMaxSize()
            .padding(12.dp)
    ) {

        Column {
            CommonTopBar(title = "Watch List")
                marketState.coin?.let { status ->
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        item {
                            MarketStatusBar(
                                marketStatus1h = status.priceChange1h!!,
                                marketStatus1d = status.priceChange1d!!,
                                marketStatus1w = status.priceChange1w!!,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 18.dp, bottom = 12.dp)
                            )
                        }
                    }
                }


                Divider(color = LighterGray, modifier = Modifier.padding(horizontal = 10.dp))
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = isRefresh),
                onRefresh = { watchListViewModel.refresh() }) {
                LazyColumn {
                    items(watchListState.value.coin) { coin ->
                        WatchlistItem(
                            modifier = Modifier
                                .combinedClickable(
                                    onClick = {
                                        navController.navigate(Screens.CoinDetailScreen.route + "/${coin.id}")
                                    },
                                ),
                            icon = coin.icon!!,
                            coinName = coin.name!!,
                            symbol = coin.symbol!!,
                            rank = coin.rank.toString(),
                            marketStatus = coin.priceChange1d ?: 0.0
                        )
                    }
                }
            }

        }
        if (marketState.isLoading) {
            CircularProgressIndicator(modifier = Modifier
                .align(Alignment.Center),
                color = CustomGreen
            )
        }

        if(marketState.error.isNotEmpty()) {
            Text(
                text = marketState.error,
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.Center)
            )
        }
    }

}