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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mathroda.common.components.CommonTopBar
import com.mathroda.common.theme.DarkGray
import com.mathroda.common.theme.LighterGray
import com.mathroda.favorite_coins.FavoriteCoinsViewModel
import com.mathroda.favorite_coins.components.common.MarketStatusBar
import com.mathroda.favorite_coins.components.common.WatchlistItem
import org.koin.androidx.compose.koinViewModel

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun WatchListAuthedUsers(
    viewModel: FavoriteCoinsViewModel,
    navigateToCoinDetails: (String) -> Unit
) {
    val watchListState by viewModel.state.collectAsState()
    val isRefresh by viewModel.isRefresh.collectAsState()
    val marketState by viewModel.marketStatus.collectAsState()

    Column(
        modifier = Modifier
            .background(DarkGray)
            .fillMaxSize()
            .padding(12.dp)
    ) {


        CommonTopBar(title = "Favorite Coins")


        Column(modifier = Modifier.fillMaxWidth()) {
            MarketStatusBar(
                marketStatus1h = marketState.coin?.priceChange1h ?: 0.0,
                marketStatus1d = marketState.coin?.priceChange1d ?: 0.0,
                marketStatus1w = marketState.coin?.priceChange1w ?: 0.0,
                isLoading = marketState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp, bottom = 12.dp)
            )
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
                    items(watchListState.coin) { coin ->
                        WatchlistItem(
                            icon = coin.icon,
                            coinName = coin.name,
                            symbol = coin.symbol,
                            rank = coin.rank.toString(),
                            marketStatus = coin.priceChanged1d,
                            onItemClick = { navigateToCoinDetails(coin.coinId) }
                        )
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
            }
        }
    }
}