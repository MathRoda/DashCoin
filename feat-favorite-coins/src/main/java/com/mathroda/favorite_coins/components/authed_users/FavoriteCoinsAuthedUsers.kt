package com.mathroda.favorite_coins.components.authed_users

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.mathroda.common.components.InternetConnectivityManger
import com.mathroda.common.navigation.Destinations
import com.mathroda.common.theme.DarkGray
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
    val watchListState by viewModel.state.collectAsState()
    val isRefresh by viewModel.isRefresh.collectAsState()
    val marketState by viewModel.marketStatus

    Column(
        modifier = Modifier
            .background(DarkGray)
            .fillMaxSize()
            .padding(12.dp)
    ) {


        CommonTopBar(title = "Favorite Coins")

        marketState.coin?.let { status ->
            Column(modifier = Modifier.fillMaxWidth()) {
                MarketStatusBar(
                    marketStatus1h = status.priceChange1h,
                    marketStatus1d = status.priceChange1d,
                    marketStatus1w = status.priceChange1w,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp, bottom = 12.dp)
                )
            }
        }


        Divider(
            color = LighterGray,
            modifier = Modifier.padding(horizontal = 10.dp)
        )

        if (watchListState.coin.isNotEmpty()) {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = isRefresh),
                onRefresh = { viewModel.refresh() }) {
                LazyColumn {
                    items(watchListState.coin) { state ->
                        state.coin.let { coin ->
                            WatchlistItem(
                                icon = coin.icon,
                                coinName = coin.name,
                                symbol = coin.symbol,
                                rank = coin.rank.toString(),
                                marketStatus = coin.priceChanged1d,
                                onItemClick = { navController.navigate(Destinations.CoinDetailScreen.route + "/${coin.coinId}") }
                            )
                        }
                    }
                }
            }
        }


       /* if (watchListState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkGray),
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = CustomGreen
                )
            }
        } */

        if (marketState.error.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkGray),
            ) {
                Text(
                    text = marketState.error,
                    color = MaterialTheme.colors.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.Center)
                )

                InternetConnectivityManger {
                    viewModel.refresh()
                }
            }
        }
    }
}