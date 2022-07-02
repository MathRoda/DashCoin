package com.mathroda.dashcoin.presentation.coins_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mathroda.dashcoin.navigation.Screens
import com.mathroda.dashcoin.presentation.coins_screen.components.CoinsItem
import com.mathroda.dashcoin.presentation.coins_screen.components.SearchBar
import com.mathroda.dashcoin.presentation.coins_screen.components.TopBar
import com.mathroda.dashcoin.presentation.coins_screen.viewmodel.CoinsViewModel
import com.mathroda.dashcoin.presentation.ui.theme.CustomGreen
import com.mathroda.dashcoin.presentation.ui.theme.DarkGray

@Composable
fun CoinScreen(
    viewModel: CoinsViewModel = hiltViewModel(),
    navController: NavController
) {

    val state = viewModel.state.collectAsState()
    val isRefreshing by viewModel.isRefresh.collectAsState()
    val searchCoin = remember { mutableStateOf(TextFieldValue(""))}

    Box(
        modifier = Modifier
            .background(DarkGray)
            .fillMaxSize()
    ) {
        Column {
            TopBar(title = "Live Prices")
            SearchBar(
                hint = "Search...",
                modifier = Modifier
                    .fillMaxWidth(),
                state = searchCoin
            )
            val isBeingSearched = searchCoin.value.text
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                onRefresh = { viewModel.refresh() }) {

                LazyColumn {
                    items(items = state.value.coins.filter {
                        it.name.contains(isBeingSearched, ignoreCase = true) ||
                        it.id.contains(isBeingSearched, ignoreCase = true) ||
                        it.symbol.contains(isBeingSearched, ignoreCase = true)
                    }, key = {it.id}) { coins ->
                        CoinsItem(
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
            CircularProgressIndicator(modifier = Modifier
                .align(Alignment.Center),
                color = CustomGreen
            )
        }

        if(state.value.error.isNotEmpty()) {
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

}
