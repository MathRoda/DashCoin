package com.mathroda.favorite_coins

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.common.state.DialogState
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
import kotlinx.coroutines.flow.first
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

    private val _dialogState = MutableStateFlow<DialogState>(DialogState.Close)
    val dialogState = _dialogState.asStateFlow()

    fun init() {
        userState()
        updateFavoriteCoins()
    }

    private fun userState() {
        viewModelScope.launch(Dispatchers.IO) {
            val userState = dashCoinUseCases.userStateProvider()
            _authState.update { userState }
            getAllCoins(userState)
            if (userState != UserState.UnauthedUser) {
                getMarketStatus()
            }
        }
    }

    private fun getAllCoins(
        user: UserState
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            when(user) {
                is UserState.UnauthedUser -> Unit
                is UserState.AuthedUser -> getAllFavoriteCoinsAuthed()
                is UserState.PremiumUser -> getAllFavoriteCoinsPremium()
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

            coins.map { favoriteCoinState ->
                if (!favoriteCoinState.updated) {
                    val id = favoriteCoinState.coin.coinId
                    val result = dashCoinRepository.getCoinByIdRemoteFlow(id).first()
                    if (result is Resource.Success && result.data != null) {
                        result.data?.run { dashCoinRepository.upsertFavoriteCoin(this.toFavoriteCoin()) }
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
            dashCoinRepository.getCoinByIdRemoteFlow("bitcoin").collect { result ->
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

    fun updateDeleteAllDialog(
        state: DialogState
    ) {
        if (_state.value.coin.isEmpty()) {
            return
        }

        _dialogState.update { state }
    }

    fun deleteAllFavoriteCoins() {
        viewModelScope.launch(Dispatchers.IO) {
            dashCoinRepository.removeAllFavoriteCoins()
        }
    }
}