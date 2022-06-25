package com.mathroda.dashcoin.presentation.coin_detail.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.dashcoin.common.Constants
import com.mathroda.dashcoin.common.Resource
import com.mathroda.dashcoin.domain.use_case.get_chart.GetChartUseCase
import com.mathroda.dashcoin.domain.use_case.get_coin.GetCoinUseCase
import com.mathroda.dashcoin.presentation.coin_detail.state.ChartState
import com.mathroda.dashcoin.presentation.coin_detail.state.CoinState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(
    private val getCoinUseCase: GetCoinUseCase,
    private val getChartUseCase: GetChartUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _coinState = mutableStateOf(CoinState())
    val coinState: State<CoinState> = _coinState

    private val _chartState = mutableStateOf(ChartState())
    val chartState: State<ChartState> = _chartState

    init {
        savedStateHandle.get<String>(Constants.PARAM_COIN_ID)?.let { coinId ->
            getCoin(coinId)
            getChart(coinId)
        }
    }


   private fun getCoin(coinId: String) {
        getCoinUseCase(coinId).onEach { result ->
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
        getChartUseCase(coinId).onEach { result ->
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
}