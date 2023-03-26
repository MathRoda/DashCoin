package com.mathroda.favorite_coins

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.common.state.MarketState
import com.mathroda.core.state.UserState
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.datasource.providers.ProvidersRepository
import com.mathroda.favorite_coins.state.WatchListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteCoinsViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val dashCoinRepository: DashCoinRepository,
    private val providersRepository: ProvidersRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WatchListState())
    val state: StateFlow<WatchListState> = _state.asStateFlow()

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean> = _isRefresh.asStateFlow()

    private val _marketStatus = mutableStateOf(MarketState())
    val marketStatus: State<MarketState> = _marketStatus

    private val _authState = mutableStateOf<UserState>(UserState.UnauthedUser)
    val authState:State<UserState> = _authState


    private var getCoinJob: Job? = null

    init {
        getMarketStatus()
    }


    private fun getAllCoins() {
        getCoinJob?.cancel()
        getCoinJob = viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.getCoinFavorite().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let { data ->
                            _state.update {
                                it.copy(
                                    coin = data,
                                    isLoading = false,
                                    error = ""
                                )
                            }

                            firebaseRepository.updateFavoriteCoinsCount(data.size)
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                error = result.message ?: "Unknown Error",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }


    fun refresh() {
        viewModelScope.launch {
            _isRefresh.emit(true)
            getMarketStatus()
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
                   // _marketStatus.value = MarketState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

      fun userState() {
        viewModelScope.launch {
           providersRepository.userStateProvider(
               function = { getAllCoins() }
           ).collect { userState ->
               when(userState) {
                   is UserState.UnauthedUser -> _authState.value = userState
                   is UserState.AuthedUser -> _authState.value = userState
                   is UserState.PremiumUser -> _authState.value = userState
               }
           }
        }
    }
}