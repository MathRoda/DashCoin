package com.mathroda.coin_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.coin_detail.state.CoinState
import com.mathroda.common.events.FavoriteCoinEvents
import com.mathroda.common.state.MarketState
import com.mathroda.core.util.Constants.PARAM_COIN_ID
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.domain.CoinById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val dashCoinRepository: DashCoinRepository,
    private val firebaseRepository: FirebaseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _coinState = mutableStateOf(CoinState())
    val coinState: State<CoinState> = _coinState

    private val _chartState = mutableStateOf(com.mathroda.coin_detail.state.ChartState())
    val chartState: State<com.mathroda.coin_detail.state.ChartState> = _chartState

    private val _marketStatus = mutableStateOf(MarketState())
    val marketStatus: State<MarketState> = _marketStatus

    private val _addToFavorite = mutableStateOf("")
    val addToFavorite: State<String> = _addToFavorite

    private val _isFavoriteState = MutableStateFlow(CoinById())
    val isFavoriteState: StateFlow<CoinById> = _isFavoriteState

    val isCurrentUserExists = firebaseRepository.isCurrentUserExist()


    init {
        savedStateHandle.get<String>(PARAM_COIN_ID)?.let { coinId ->
            getChart(coinId)
            getCoin(coinId)
        }
        getMarketStatus()

    }

    private fun getCoin(coinId: String) {
        dashCoinRepository.getCoinById(coinId).onEach { result ->
            when (result) {
                is com.mathroda.core.util.Resource.Success -> {
                    _coinState.value = com.mathroda.coin_detail.state.CoinState(coin = result.data)
                }
                is com.mathroda.core.util.Resource.Error -> {
                    _coinState.value = com.mathroda.coin_detail.state.CoinState(
                        error = result.message ?: "Unexpected Error"
                    )
                }
                is com.mathroda.core.util.Resource.Loading -> {
                    _coinState.value = com.mathroda.coin_detail.state.CoinState(isLoading = true)
                    delay(300)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getChart(coinId: String) {
        dashCoinRepository.getChartsData(coinId).onEach { result ->
            when (result) {
                is com.mathroda.core.util.Resource.Success -> {
                    _chartState.value =
                        com.mathroda.coin_detail.state.ChartState(chart = result.data)
                }
                is com.mathroda.core.util.Resource.Error -> {
                    _chartState.value = com.mathroda.coin_detail.state.ChartState(
                        error = result.message ?: "Unexpected Error"
                    )
                }
                is com.mathroda.core.util.Resource.Loading -> {
                    _chartState.value = com.mathroda.coin_detail.state.ChartState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getMarketStatus() {
        dashCoinRepository.getCoinById("bitcoin").onEach { result ->
            when (result) {
                is com.mathroda.core.util.Resource.Success -> {
                    _marketStatus.value = MarketState(coin = result.data)
                }
                is com.mathroda.core.util.Resource.Error -> {
                    _marketStatus.value = MarketState(
                        error = result.message ?: "Unexpected Error"
                    )
                }
                is com.mathroda.core.util.Resource.Loading -> {
                    _marketStatus.value = MarketState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(events: FavoriteCoinEvents) {
        when (events) {

            is FavoriteCoinEvents.AddCoin -> {
                viewModelScope.launch { //dashCoinUseCases.addCoin(events.coin)
                    firebaseRepository.addCoinFavorite(events.coin).collect { result ->
                        when (result) {
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                _addToFavorite.value = "Coin successfully added to favorite! "
                            }
                            is Resource.Error -> {
                                _addToFavorite.value = result.message.toString()
                            }
                        }
                    }
                }
            }

            is FavoriteCoinEvents.DeleteCoin -> {
                viewModelScope.launch {
                    firebaseRepository.deleteCoinFavorite(events.coin).collect()
                }
            }

        }
    }

    fun isFavoriteState(coinById: CoinById) {
        viewModelScope.launch {
            firebaseRepository.isFavoriteState(coinById).collect {
                _isFavoriteState.emit(it ?: CoinById())
            }
        }
    }

}