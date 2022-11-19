package com.mathroda.favorite_coins

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.common.state.MarketState
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.core.state.AuthenticationState
import com.mathroda.favorite_coins.state.WatchListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteCoinsViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val dashCoinRepository: DashCoinRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WatchListState())
    val state: StateFlow<WatchListState> = _state

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean> = _isRefresh

    private val _marketStatus = mutableStateOf(MarketState())
    val marketStatus: State<MarketState> = _marketStatus

    private val _authState = mutableStateOf<AuthenticationState>(AuthenticationState.UnauthedUser)
    val authState:State<AuthenticationState> = _authState

    private var getCoinJob: Job? = null

    init {
        getMarketStatus()
    }


    private fun getAllCoins() {
        getCoinJob?.cancel()
        getCoinJob = firebaseRepository.getAllFavoriteCoins().onEach { result ->
            when (result) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    result.data?.let {
                        _state.emit(WatchListState(coin = it))
                    }
                }
                is Resource.Error -> {
                    _state.emit(WatchListState(error = result.message.toString()))
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
                is Resource.Success -> {
                    _marketStatus.value = MarketState(coin = result.data)
                }
                is Resource.Error -> {
                    _marketStatus.value = MarketState(
                        error = result.message ?: "Unexpected Error"
                    )
                }
                is Resource.Loading -> {
                    _marketStatus.value = MarketState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

      fun uiState() {
        viewModelScope.launch {
            firebaseRepository.isCurrentUserExist().collect {
                when (it) {

                    false -> _authState.value = AuthenticationState.UnauthedUser

                    true -> {
                        _authState.value = AuthenticationState.AuthedUser
                        getAllCoins()
                    }
                }
            }
        }
    }


}