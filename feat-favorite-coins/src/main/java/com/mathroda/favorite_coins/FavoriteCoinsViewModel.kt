package com.mathroda.favorite_coins

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.common.state.MarketState
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteCoinsViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val dashCoinRepository: DashCoinRepository
) : ViewModel() {

    private val _state = MutableStateFlow(com.mathroda.favorite_coins.state.WatchListState())
    val state: StateFlow<com.mathroda.favorite_coins.state.WatchListState> = _state

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean> = _isRefresh

    private val _marketStatus = mutableStateOf(MarketState())
    val marketStatus: State<MarketState> = _marketStatus

    val isCurrentUserExists = firebaseRepository.isCurrentUserExist()


    private var getCoinJob: Job? = null

    init {
        getAllCoins()
        getMarketStatus()
    }


    private fun getAllCoins() {
        getCoinJob?.cancel()
        getCoinJob = firebaseRepository.getCoinFavorite().onEach { result ->
            when (result) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    result.data?.let {
                        _state.emit(com.mathroda.favorite_coins.state.WatchListState(coin = it))
                    }
                }
                is Resource.Error -> {
                    _state.emit(com.mathroda.favorite_coins.state.WatchListState(error = result.message))
                }
            }
        }.launchIn(viewModelScope)
    }


    fun refresh() {
        viewModelScope.launch {
            _isRefresh.emit(true)
            getAllCoins()
            _isRefresh.emit(false)
        }

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


}