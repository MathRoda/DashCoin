package com.mathroda.coin_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mathroda.coin_detail.components.*
import com.mathroda.common.components.CustomDialog
import com.mathroda.common.events.FavoriteCoinEvents
import com.mathroda.common.theme.DarkGray
import com.mathroda.core.util.numbersToCurrency
import com.mathroda.core.util.numbersToFormat

@Composable
fun CoinDetailScreen(
    viewModel: CoinDetailViewModel = hiltViewModel(),
    navController: NavController
) {

    val coinState = viewModel.coinState.value
    val chartsState = viewModel.chartState.value
    val uriHandler = LocalUriHandler.current

    Box(
        modifier = Modifier
            .background(DarkGray)
            .fillMaxSize()
            .padding(12.dp)
    ) {
        coinState.coin?.let { coin ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    TopBarCoinDetail(
                        navController = navController,
                        coinSymbol = coin.symbol,
                        icon = coin.icon,
                        isFavorite = viewModel.isFavoriteState.value,
                        onCLick = {
                            viewModel.onFavoriteClick(
                                coin = coin
                            )
                        }
                    )

                    CustomDialog(
                        dialogState = viewModel.dialogState,
                        coin = coin,
                        navController = navController
                    ) {
                        viewModel.onEvent(FavoriteCoinEvents.DeleteCoin(coin))
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

                    Chart(
                        oneDayChange = coin.priceChange1d,
                        context = LocalContext.current,
                        charts = chartsState
                    )

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


