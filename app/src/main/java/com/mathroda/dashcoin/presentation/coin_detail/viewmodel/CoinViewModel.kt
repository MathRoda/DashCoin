package com.mathroda.dashcoin.presentation.coin_detail.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.dashcoin.common.Constants
import com.mathroda.dashcoin.common.Resource
import com.mathroda.dashcoin.domain.use_case.get_coin.GetCoinUseCase
import com.mathroda.dashcoin.domain.use_case.get_coins.GetCoinsUseCase
import com.mathroda.dashcoin.presentation.coin_detail.state.CoinState
import com.mathroda.dashcoin.presentation.coins_screen.state.CoinsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(
    private val getCoinUseCase: GetCoinUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = mutableStateOf(CoinState())
    val state: State<CoinState> = _state

    init {
        savedStateHandle.get<String>(Constants.PARAM_COIN_ID)?.let { coinId ->
            getCoin(coinId)
        }
    }


   private fun getCoin(coinId: String) {
        getCoinUseCase(coinId).onEach { result ->
            when(result) {
                is Resource.Success ->{
                    _state.value = CoinState(coin = result.data)
                }
                is Resource.Error ->{
                    _state.value = CoinState(
                        error = result.message?: "Unexpected Error")
                }
                is Resource.Loading ->{
                    _state.value = CoinState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}