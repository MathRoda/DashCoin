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
import com.mathroda.domain.model.FavoriteCoin
import com.mathroda.domain.model.toFavoriteCoin
import com.mathroda.favorite_coins.state.FavoriteCoinsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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

    private val _authState = mutableStateOf<UserState>(UserState.UnauthedUser)
    val authState:State<UserState> = _authState

    fun userState() {
        viewModelScope.launch(Dispatchers.IO) {
            dashCoinUseCases.userStateProvider(
                function = {
                    getAllCoins()
                    getMarketStatus()
                }
            ).collect { userState -> _authState.value = userState}
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
        dashCoinRepository.getFavoriteCoins().collect { result ->
            updateFavoriteCoinsState(coins = result)
            updateFavoriteCoins()
        }
    }

    private suspend fun getAllFavoriteCoinsPremium() {
        dashCoinUseCases.getAllFavoriteCoins().collect { result ->
            when (result) {
                is Resource.Loading -> updateFavoriteCoinsState(isLoading = true)
                is Resource.Success -> {
                    result.data?.let { data -> updateFavoriteCoinsState(coins = data) }
                    updateFavoriteCoins()
                }
                is Resource.Error -> updateFavoriteCoinsState(error = result.message)
            }
        }
    }

    private suspend fun updateFavoriteCoins() {
        _state.value.coin.forEach { favoriteCoin ->
            val id = favoriteCoin.coinId
            val result = dashCoinRepository.getCoinByIdRemote(id).firstOrNull()

            if (result is Resource.Success) {
                result.data?.let { dashCoinRepository.addFavoriteCoin(it.toFavoriteCoin()) }
            }
        }
    }
    private fun getMarketStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            dashCoinRepository.getCoinByIdRemote("bitcoin").firstOrNull()?.let { result ->
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
        viewModelScope.launch {
            _isRefresh.update { true }
            getMarketStatus()
            getAllCoins()
            _isRefresh.update { false }
        }

    }

    private fun updateFavoriteCoinsState(
        isLoading: Boolean? = null,
        coins: List<FavoriteCoin>? = null,
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