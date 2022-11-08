package com.mathroda.dashcoin.presentation.watchlist_screen.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.data.dto.toCoinDetail
import com.mathroda.dashcoin.domain.model.CoinById
import com.mathroda.dashcoin.domain.repository.DashCoinRepository
import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import com.mathroda.dashcoin.presentation.watchlist_screen.events.WatchListEvents
import com.mathroda.dashcoin.presentation.watchlist_screen.state.WatchListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchListViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val dashCoinRepository: DashCoinRepository
): ViewModel() {

    private val _state = MutableStateFlow(WatchListState())
    val state: StateFlow<WatchListState> = _state

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean> = _isRefresh

    private val _isFavoriteState = MutableStateFlow(CoinById())
    val isFavoriteState: StateFlow<CoinById> = _isFavoriteState

    private val _addToFavorite = mutableStateOf("")
    val addToFavorite: State<String> = _addToFavorite

    val isCurrentUserExists = firebaseRepository.isCurrentUserExist()

    private var getCoinJob: Job? = null

    init {
        getAllCoins()
    }

    fun onEvent(events: WatchListEvents) {
        when(events) {

            is WatchListEvents.AddCoin -> {
                viewModelScope.launch { //dashCoinUseCases.addCoin(events.coin)
                    firebaseRepository.addCoinFavorite(events.coin).collect { result ->
                        when(result) {
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                _addToFavorite.value = "Coin successfully added to favorite! " }
                            is Resource.Error -> {
                                _addToFavorite.value = result.message.toString()
                            }
                        }
                    }
                }
            }

            is WatchListEvents.DeleteCoin -> {
                viewModelScope.launch {
                    firebaseRepository.deleteCoinFavorite(events.coin).collect()
                }
            }

        }
    }

    private fun getAllCoins() {
        getCoinJob?.cancel()
        getCoinJob = firebaseRepository.getCoinFavorite().onEach { result ->
                when(result) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        result.data?.let {
                            _state.emit(WatchListState(coin = result.data))
                        }
                    }
                    is Resource.Error -> {
                        _state.emit(WatchListState(error = result.message))
                    }
                }
            }.launchIn(viewModelScope)
    }



    fun isFavoriteState(coinById: CoinById) {
        viewModelScope.launch {
            firebaseRepository.isFavoriteState(coinById).collect {
                _isFavoriteState.emit(it?: CoinById())
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefresh.emit(true)
            getAllCoins()
            _isRefresh.emit(false)
        }

    }



}