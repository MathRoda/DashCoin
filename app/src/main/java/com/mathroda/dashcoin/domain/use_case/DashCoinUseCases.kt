package com.mathroda.dashcoin.domain.use_case

import com.mathroda.dashcoin.domain.use_case.database.add_coin.AddCoinUseCase
import com.mathroda.dashcoin.domain.use_case.database.delete_coin.DeleteCoinUseCase
import com.mathroda.dashcoin.domain.use_case.database.get_all.GetAllCoinsUseCase
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

    // domain/ use_case/ database
    val addCoin: AddCoinUseCase,
    val deleteCoin: DeleteCoinUseCase,
    val getAllCoins: GetAllCoinsUseCase
        )