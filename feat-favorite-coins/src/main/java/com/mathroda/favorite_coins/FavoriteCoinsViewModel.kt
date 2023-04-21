package com.mathroda.favorite_coins

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.common.state.MarketState
import com.mathroda.core.state.UserState
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.usecases.DashCoinUseCases
import com.mathroda.domain.model.toFavoriteCoin
import com.mathroda.favorite_coins.state.CoinState
import com.mathroda.favorite_coins.state.FavoriteCoinsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteCoinsViewModel @Inject constructor(
    private val dashCoinRepository: DashCoinRepository,
    private val dashCoinUseCases: DashCoinUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(FavoriteCoinsState())
    val state: StateFlow<FavoriteCoinsState> = _state.asStateFlow()

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean> = _isRefresh.asStateFlow()

    private val _marketStatus = mutableStateOf(MarketState())
    val marketStatus: State<MarketState> = _marketStatus

    private val _authState = MutableStateFlow<UserState>(UserState.UnauthedUser)
    val authState = _authState.asStateFlow()

    fun init() {
        userState()
        getAllCoins()
        getMarketStatus()
        updateFavoriteCoins()
    }

    private fun userState() {
        viewModelScope.launch(Dispatchers.IO) {
            dashCoinUseCases.userStateProvider(
                function = {}
            ).collect { userState -> _authState.update { userState } }
        }
    }

    private fun getAllCoins() {
        viewModelScope.launch(Dispatchers.IO) {
            dashCoinUseCases.userStateProvider(
                function = {}
            ).collect { user ->
                when(user) {
                    is UserState.UnauthedUser -> {}
                    is UserState.AuthedUser -> getAllFavoriteCoinsAuthed()
                    is UserState.PremiumUser -> getAllFavoriteCoinsPremium()
                }

            }
        }
    }

    private suspend fun getAllFavoriteCoinsAuthed() {
        dashCoinRepository.getFlowFavoriteCoins().collect { result ->
            val state = result.map { CoinState().copy(coin = it) }
            updateFavoriteCoinsState(coins = state)
        }
    }

    private suspend fun getAllFavoriteCoinsPremium() {
        dashCoinUseCases.getAllFavoriteCoins().collect { result ->
            when (result) {
                is Resource.Loading -> updateFavoriteCoinsState(isLoading = true)
                is Resource.Success -> {
                    result.data?.let { data ->
                        val state = data.map { CoinState().copy(coin = it) }
                        updateFavoriteCoinsState(coins = state)
                    }
                }
                is Resource.Error -> updateFavoriteCoinsState(error = result.message)
            }
        }
    }

    private fun updateFavoriteCoins() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<CoinState>()
            val coins = _state.value.coin
            if (coins.isEmpty()) {
                return@launch
            }

            coins.forEach { favoriteCoinState ->
                if (favoriteCoinState.updated) {
                    return@forEach
                }

                val id = favoriteCoinState.coin.coinId
                dashCoinRepository.getCoinByIdRemote(id).collect { result ->
                    if (result is Resource.Success && result.data != null) {
                        result.data?.run { dashCoinRepository.addFavoriteCoin(this.toFavoriteCoin()) }
                        val state = favoriteCoinState.copy(updated = true)
                        list.add(state)
                    }
                }
            }

            updateFavoriteCoinsState(
                coins = list
            )
        }
    }
    private fun getMarketStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            dashCoinRepository.getCoinByIdRemote("bitcoin").collect { result ->
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
            }
        }
    }

    fun refresh() {
        /*viewModelScope.launch {
            _isRefresh.update { true }
            getMarketStatus()
            getAllCoins()
            _isRefresh.update { false }
        }*/

    }

    private fun updateFavoriteCoinsState(
        isLoading: Boolean? = null,
        coins: List<CoinState>? = null,
        isEmpty: Boolean? = null,
        error: String? = null
    ) {
        _state.update {
            it.copy(
                isLoading = isLoading ?: it.isLoading,
                coin = coins ?: it.coin,
                isEmpty = isEmpty ?: it.isEmpty,
                error = error ?: it.error
            )
        }
    }
}