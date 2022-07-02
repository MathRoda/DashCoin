package com.mathroda.dashcoin.presentation.coins_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.use_case.DashCoinUseCases
import com.mathroda.dashcoin.presentation.coins_screen.state.CoinsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Collections.emptyList
import javax.inject.Inject

@HiltViewModel
class CoinsViewModel @Inject constructor(
    private val dashCoinUseCases: DashCoinUseCases
): ViewModel() {

    private val _state = MutableStateFlow(CoinsState())
    val state: StateFlow<CoinsState> = _state

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean> = _isRefresh

    init {
        getCoins()
    }


   private fun getCoins() {
        dashCoinUseCases.getCoins().onEach { result ->
            when(result) {
                is Resource.Success ->{
                    _state.emit(CoinsState(coins = result.data?: emptyList()))
                }
                is Resource.Error ->{
                    _state.emit(
                        CoinsState(
                            error = result.message?: "Unexpected Error")
                    )

                }
                is Resource.Loading ->{
                    _state.emit(
                        CoinsState(isLoading = true)
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefresh.emit(true)
            getCoins()
            _isRefresh.emit(false)
        }

    }


}