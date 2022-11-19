package com.mathroda.coin_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.mathroda.coin_detail.components.Chart
import com.mathroda.coin_detail.components.CoinDetailSection
import com.mathroda.coin_detail.components.CoinInformation
import com.mathroda.common.R
import com.mathroda.common.components.CustomDialog
import com.mathroda.common.events.FavoriteCoinEvents
import com.mathroda.common.util.numbersToCurrency
import com.mathroda.common.util.numbersToFormat
import com.talhafaki.composablesweettoast.util.SweetToastUtil

@Composable
fun CoinDetailScreen(
    viewModel: CoinDetailViewModel = hiltViewModel(),
    navController: NavController
) {

    val coinState = viewModel.coinState.value
    val chartsState = viewModel.chartState.value
    var isFavorite by remember { mutableStateOf(false) }
    var sideEffect by remember { mutableStateOf(false) }
    val lottieComp by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loading_main))
    val lottieProgress by animateLottieCompositionAsState(
        composition = lottieComp,
        iterations = LottieConstants.IterateForever,
    )
    val isUserExist = viewModel.isCurrentUserExists.collectAsState(initial = false)

    Box(
        modifier = Modifier
            .background(com.mathroda.common.theme.DarkGray)
            .fillMaxSize()
            .padding(12.dp)
    ) {
        coinState.coin?.let { coin ->

            /**
             * first thing when the coin detail screen renders it checks
             * if the coin exist in Firestore and collect the flow as State
             */
            viewModel.isFavoriteState(coin)
            val isFav = viewModel.isFavoriteState.collectAsState()
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {

                    isFavorite = isFav.value.id == coin.id
                    val openDialogCustom = remember { mutableStateOf(false) }
                    com.mathroda.coin_detail.components.TopBarCoinDetail(
                        coinSymbol = coin.symbol!!,
                        icon = coin.icon!!,
                        navController = navController,
                        isFavorite = isFavorite,
                        onCLick = {
                            isFavorite = !isFavorite

                            if (isFavorite) {
                                if (isUserExist.value) {
                                    viewModel.onEvent(FavoriteCoinEvents.AddCoin(coin))
                                } else {
                                    sideEffect = !isUserExist.value
                                }

                            } else openDialogCustom.value = true
                        }
                    )
                    if (openDialogCustom.value) {
                        CustomDialog(
                            openDialogCustom = openDialogCustom,
                            coin = coin,
                            navController = navController
                        ) {
                            viewModel.onEvent(FavoriteCoinEvents.DeleteCoin(coin))
                        }
                    }
                    CoinDetailSection(
                        price = coin.price!!,
                        priceChange = coin.priceChange1d!!
                    )

                    Chart(
                        oneDayChange = coin.priceChange1d!!,
                        context = LocalContext.current,
                        charts = chartsState.chart
                    )

                    CoinInformation(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = com.mathroda.common.theme.LighterGray,
                                shape = RoundedCornerShape(25.dp)
                            )
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                        rank = "${coin.rank}",
                        volume = numbersToCurrency(coin.volume!!.toInt())!!,
                        marketCap = numbersToCurrency(coin.marketCap!!.toInt())!!,
                        availableSupply = "${numbersToFormat(coin.availableSupply!!.toInt())} ${coin.symbol}",
                        totalSupply = "${numbersToFormat(coin.totalSupply!!.toInt())} ${coin.symbol}"
                    )

                    val uriHandler = LocalUriHandler.current
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        com.mathroda.coin_detail.components.LinkButton(
                            title = "Twitter",
                            modifier = Modifier
                                .padding(start = 20.dp, bottom = 20.dp, top = 20.dp)
                                .clip(RoundedCornerShape(35.dp))
                                .height(45.dp)
                                .background(com.mathroda.common.theme.Twitter)
                                .weight(1f)
                                .clickable {
                                    uriHandler.openUri(coin.twitterUrl!!)
                                }
                        )

                        com.mathroda.coin_detail.components.LinkButton(
                            title = "Website",
                            modifier = Modifier
                                .padding(start = 20.dp, bottom = 20.dp, top = 20.dp)
                                .clip(RoundedCornerShape(35.dp))
                                .height(45.dp)
                                .background(com.mathroda.common.theme.LighterGray)
                                .weight(1f)
                                .clickable {
                                    uriHandler.openUri(coin.websiteUrl!!)
                                }
                        )
                    }
                }
            }
        }

        if (sideEffect) {
            SweetToastUtil.SweetWarning(
                padding = PaddingValues(24.dp),
                message = "Please Login First"
            )
            sideEffect = !sideEffect
        }

        if (coinState.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                LottieAnimation(
                    composition = lottieComp,
                    progress = { lottieProgress },
                )
            }
        }

        if (coinState.error.isNotBlank()) {
            Text(
                text = coinState.error,
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


