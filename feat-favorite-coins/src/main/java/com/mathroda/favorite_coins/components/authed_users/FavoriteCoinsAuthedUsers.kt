package com.mathroda.favorite_coins.components.authed_users

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import com.mathroda.common.components.CommonTopBar
import com.mathroda.common.navigation.Destinations
import com.mathroda.common.theme.LighterGray
import com.mathroda.favorite_coins.FavoriteCoinsViewModel
import com.mathroda.favorite_coins.components.common.MarketStatusBar
import com.mathroda.favorite_coins.components.common.WatchlistItem

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun WatchListAuthedUsers(
    viewModel: FavoriteCoinsViewModel = hiltViewModel(),
    navController: NavController
) {
    val watchListState = viewModel.state.collectAsState()
    val isRefresh by viewModel.isRefresh.collectAsState()
    val marketState = viewModel.marketStatus.value

    Box(
        modifier = Modifier
            .background(com.mathroda.common.theme.DarkGray)
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


            Divider(
                color = LighterGray,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = isRefresh),
                onRefresh = { viewModel.refresh() }) {
                LazyColumn {
                    items(watchListState.value.coin) { coin ->
                        WatchlistItem(
                            modifier = Modifier
                                .combinedClickable(
                                    onClick = {
                                        navController.navigate(Destinations.CoinDetailScreen.route + "/${coin.id}")
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
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center),
                color = com.mathroda.common.theme.CustomGreen
            )
        }

        if (marketState.error.isNotEmpty()) {
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