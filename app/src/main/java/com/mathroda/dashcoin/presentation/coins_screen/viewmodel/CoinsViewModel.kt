package com.mathroda.dashcoin.presentation.coins_screen.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.dashcoin.common.Resource
import com.mathroda.dashcoin.domain.use_case.get_coins.GetCoinsUseCase
import com.mathroda.dashcoin.presentation.coins_screen.state.CoinsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoinsViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase
): ViewModel() {

    private val _state = mutableStateOf(CoinsState())
    val state: State<CoinsState> = _state

    init {
        getCoins()
    }


   private fun getCoins() {
        getCoinsUseCase().onEach { result ->
            when(result) {
                is Resource.Success ->{
                    _state.value = CoinsState(coins = result.data?: emptyList())
                }
                is Resource.Error ->{
                    _state.value = CoinsState(
                        error = result.message?: "Unexpected Error")
                }
                is Resource.Loading ->{
                    _state.value = CoinsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}