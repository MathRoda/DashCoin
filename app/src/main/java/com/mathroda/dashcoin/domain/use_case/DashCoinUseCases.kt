package com.mathroda.dashcoin.domain.use_case

import com.mathroda.dashcoin.domain.use_case.remote.get_chart.GetChartUseCase
import com.mathroda.dashcoin.domain.use_case.remote.get_coin.GetCoinUseCase
import com.mathroda.dashcoin.domain.use_case.remote.get_coins.GetCoinsUseCase
import com.mathroda.dashcoin.domain.use_case.remote.get_news.GetNewsUseCase

data class DashCoinUseCases (

    // domain/ use_case/ remote
    val getCoins: GetCoinsUseCase,
    val getCoin: GetCoinUseCase,
    val getChart: GetChartUseCase,
    val getNews: GetNewsUseCase,
        )