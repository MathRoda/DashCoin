package com.mathroda.dashcoin.presentation.coin_detail.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.dashcoin.core.util.Constants
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.use_case.DashCoinUseCases
import com.mathroda.dashcoin.presentation.coin_detail.state.ChartState
import com.mathroda.dashcoin.presentation.coin_detail.state.CoinState
import com.mathroda.dashcoin.presentation.watchlist_screen.state.MarketState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(
    private val dashCoinUseCases: DashCoinUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _coinState = mutableStateOf(CoinState())
    val coinState: State<CoinState> = _coinState

    private val _chartState = mutableStateOf(ChartState())
    val chartState: State<ChartState> = _chartState

    private val _marketStatus = mutableStateOf(MarketState())
    val marketStatus: State<MarketState> = _marketStatus




    /**
     * notes
     * ive to put getCoin in saved instance init block after testing
     * remove the initial value from coinId: String = "bitcoin"
     */
    init {
        savedStateHandle.get<String>(Constants.PARAM_COIN_ID)?.let { coinId ->
            getChart(coinId)
            getCoin(coinId)
        }
        getMarketStatus()

    }


     private fun getCoin(coinId: String) {
        dashCoinUseCases.getCoin(coinId).onEach { result ->
            when(result) {
                is Resource.Success ->{
                    _coinState.value = CoinState(coin = result.data)
                }
                is Resource.Error ->{
                    _coinState.value = CoinState(
                        error = result.message?: "Unexpected Error")
                }
                is Resource.Loading ->{
                    _coinState.value = CoinState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getChart(coinId: String) {
        dashCoinUseCases.getChart(coinId).onEach { result ->
            when(result) {
                is Resource.Success ->{
                    _chartState.value = ChartState(chart = result.data)
                }
                is Resource.Error ->{
                    _chartState.value = ChartState(
                        error = result.message?: "Unexpected Error")
                }
                is Resource.Loading ->{
                    _chartState.value = ChartState( isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getMarketStatus() {
        dashCoinUseCases.getCoin("bitcoin").onEach { result ->
            when(result) {
                is Resource.Success ->{
                    _marketStatus.value = MarketState(coin = result.data)
                }
                is Resource.Error ->{
                    _marketStatus.value = MarketState(
                        error = result.message?: "Unexpected Error")
                }
                is Resource.Loading ->{
                    _marketStatus.value = MarketState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}