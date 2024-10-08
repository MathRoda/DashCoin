package com.mathroda.favorites.components.premium_users

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
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mathroda.common.components.CommonTopBar
import com.mathroda.common.theme.DarkGray
import com.mathroda.common.theme.LighterGray
import com.mathroda.favorites.FavoriteCoinsViewModel
import com.mathroda.favorites.components.common.MarketStatusBar
import com.mathroda.favorites.components.common.WatchlistItem

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun WatchListPremiumUsers(
    viewModel: FavoriteCoinsViewModel,
    navigateToCoinDetails: (String) -> Unit
) {
    val watchListState by viewModel.state.collectAsState()
    val isRefreshing by viewModel.isRefresh.collectAsState()
    val marketState by viewModel.marketStatus.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refresh() }
    )

    Box(
        modifier = Modifier
            .background(DarkGray)
            .fillMaxSize()
            .padding(12.dp)
    ) {

        Column {
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .pullRefresh(pullRefreshState)
            ) {
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

                PullRefreshIndicator(
                    refreshing = viewModel.state.value.isLoading,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }

        }

      /*  if (watchListState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center),
                color = CustomGreen
            )
        } */

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