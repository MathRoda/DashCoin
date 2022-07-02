package com.mathroda.dashcoin.presentation.watchlist_screen.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.dashcoin.domain.model.CoinById
import com.mathroda.dashcoin.domain.use_case.DashCoinUseCases
import com.mathroda.dashcoin.presentation.watchlist_screen.events.WatchListEvents
import com.mathroda.dashcoin.presentation.watchlist_screen.state.WatchListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchListViewModel @Inject constructor(
    private val dashCoinUseCases: DashCoinUseCases
): ViewModel() {

    private val _state = mutableStateOf(WatchListState())
    val state: State<WatchListState> = _state

    private var recentlyDeletedCoin: CoinById? = null
    private var getNotesJob: Job? = null

    init {
        getAllCoins()
    }

    fun onEvent(events: WatchListEvents) {
        when(events) {

            is WatchListEvents.AddCoin -> {
                viewModelScope.launch {
                    dashCoinUseCases.addCoin(events.coin)
                }
            }

            is WatchListEvents.DeleteCoin -> {
                viewModelScope.launch {
                    dashCoinUseCases.deleteCoin(events.coin)
                    recentlyDeletedCoin = events.coin
                }
            }

            is WatchListEvents.RestoreDeletedCoin -> {
                viewModelScope.launch {
                    dashCoinUseCases.addCoin(recentlyDeletedCoin ?: return@launch)
                    recentlyDeletedCoin = null
                }
            }
        }
    }

    private fun getAllCoins() {
        getNotesJob?.cancel()
        getNotesJob = dashCoinUseCases.getAllCoins().onEach {
            _state.value = WatchListState(it)
        }.launchIn(viewModelScope)
    }

}