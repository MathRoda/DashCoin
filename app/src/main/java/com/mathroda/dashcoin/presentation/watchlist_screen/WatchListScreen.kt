package com.mathroda.dashcoin.presentation.watchlist_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mathroda.dashcoin.navigation.main.Screens
import com.mathroda.dashcoin.presentation.coin_detail.viewmodel.CoinViewModel
import com.mathroda.dashcoin.presentation.coins_screen.components.TopBar
import com.mathroda.dashcoin.presentation.ui.theme.CustomGreen
import com.mathroda.dashcoin.presentation.ui.theme.DarkGray
import com.mathroda.dashcoin.presentation.ui.theme.LighterGray
import com.mathroda.dashcoin.presentation.watchlist_screen.components.MarketStatusBar
import com.mathroda.dashcoin.presentation.watchlist_screen.components.WatchlistItem
import com.mathroda.dashcoin.presentation.watchlist_screen.viewmodel.WatchListViewModel

@ExperimentalMaterialApi
@Composable
fun WatchListScreen(
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
            TopBar(title = "Watch List")
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
                            icon = coin.icon!!,
                            coinName = coin.name!!,
                            symbol = coin.symbol!!,
                            rank = coin.rank.toString(),
                            onClick = {
                                navController.navigate(Screens.CoinDetailScreen.route + "/${coin.id}")
                            }
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