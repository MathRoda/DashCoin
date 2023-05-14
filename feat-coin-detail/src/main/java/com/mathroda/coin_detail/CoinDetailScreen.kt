package com.mathroda.coin_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mathroda.coin_detail.components.Chart
import com.mathroda.coin_detail.components.CoinDetailScreenState
import com.mathroda.coin_detail.components.CoinDetailSection
import com.mathroda.coin_detail.components.CoinInformation
import com.mathroda.coin_detail.components.LinkButton
import com.mathroda.coin_detail.components.LoadingChartState
import com.mathroda.coin_detail.components.NotPremiumDialog
import com.mathroda.coin_detail.components.TimeRangePicker
import com.mathroda.coin_detail.components.TopBarCoinDetail
import com.mathroda.common.components.CustomDialog
import com.mathroda.common.events.FavoriteCoinEvents
import com.mathroda.common.theme.DarkGray
import com.mathroda.core.util.numbersToCurrency
import com.mathroda.core.util.numbersToFormat
import com.mathroda.domain.model.toFavoriteCoin

@Composable
fun CoinDetailScreen(
    viewModel: CoinDetailViewModel = hiltViewModel(),
    navController: NavController
) {

    val coinState = viewModel.coinState.collectAsState().value
    val chartsState = viewModel.chartState.value
    val uriHandler = LocalUriHandler.current
    val scrollState = rememberScrollState()

    LaunchedEffect(true) {
        viewModel.updateUserState()
        viewModel.updateUiState()
    }

    Box(
        modifier = Modifier
            .background(DarkGray)
            .fillMaxSize()
    ) {

        coinState.coin?.let { coin ->
            Scaffold(
                topBar = {
                    TopBarCoinDetail(
                        navController = navController,
                        coinSymbol = coin.symbol,
                        icon = coin.icon,
                        isFavorite = viewModel.isFavoriteState.value,
                        onCLick = { viewModel.onFavoriteClick(coin) }
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(8.dp)
                        .padding(paddingValues)
                ) {
                    CustomDialog(
                        dialogState = viewModel.dialogState,
                        coin = coin
                    ) {
                        viewModel.onEvent(FavoriteCoinEvents.DeleteCoin(coin.toFavoriteCoin()))
                    }

                    CoinDetailSection(
                        price = coin.price,
                        priceChange = coin.priceChange1d
                    )

                    TimeRangePicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                    ) { timeRange ->
                        viewModel.onTimeSpanChanged(timeRange)
                    }

                    if (!chartsState.isLoading){
                        Chart(
                            oneDayChange = coin.priceChange1d,
                            context = LocalContext.current,
                            charts = chartsState
                        )
                    }

                    LoadingChartState()

                    CoinInformation(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = com.mathroda.common.theme.LighterGray,
                                shape = RoundedCornerShape(25.dp)
                            )
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                        rank = "${coin.rank}",
                        volume = numbersToCurrency(coin.volume.toInt()),
                        marketCap = numbersToCurrency(coin.marketCap.toInt()),
                        availableSupply = "${numbersToFormat(coin.availableSupply.toInt())} ${coin.symbol}",
                        totalSupply = "${numbersToFormat(coin.totalSupply.toInt())} ${coin.symbol}"
                    )

                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        LinkButton(
                            title = "Twitter",
                            modifier = Modifier
                                .padding(start = 20.dp, bottom = 20.dp, top = 20.dp)
                                .clip(RoundedCornerShape(35.dp))
                                .height(45.dp)
                                .background(com.mathroda.common.theme.Twitter)
                                .weight(1f)
                                .clickable {
                                    uriHandler.openUri(coin.twitterUrl)
                                }
                        )

                        LinkButton(
                            title = "Website",
                            modifier = Modifier
                                .padding(start = 20.dp, bottom = 20.dp, top = 20.dp)
                                .clip(RoundedCornerShape(35.dp))
                                .height(45.dp)
                                .background(com.mathroda.common.theme.LighterGray)
                                .weight(1f)
                                .clickable {
                                    uriHandler.openUri(coin.websiteUrl)
                                }
                        )
                    }
                }
            }
        }

        NotPremiumDialog(dialogState = viewModel.notPremiumDialog)

        CoinDetailScreenState()
    }
}


