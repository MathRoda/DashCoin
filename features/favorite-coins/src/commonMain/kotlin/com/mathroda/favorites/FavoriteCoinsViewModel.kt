package com.mathroda.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.common.util.asyncMap
import com.mathroda.core.state.DialogState
import com.mathroda.core.state.UserState
import com.mathroda.core.util.Resource
import com.mathroda.core.util.getCurrentDateTime
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.usecases.DashCoinUseCases
import com.mathroda.domain.FavoriteCoin
import com.mathroda.domain.toFavoriteCoin
import com.mathroda.favorites.state.FavoriteCoinsState
import com.mathroda.favorites.state.MarketState
import com.mathroda.phoneshaking.PhoneShakingManger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class FavoriteCoinsViewModel(
    private val dashCoinRepository: DashCoinRepository,
    private val dashCoinUseCases: DashCoinUseCases,
    val phoneShakingManger: PhoneShakingManger
) : ViewModel() {

    private val _state = MutableStateFlow(FavoriteCoinsState())
    val state: StateFlow<FavoriteCoinsState> = _state.asStateFlow()

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean> = _isRefresh.asStateFlow()

    private val _marketStatus = MutableStateFlow(MarketState())
    val marketStatus = _marketStatus.asStateFlow()

    private val _authState = MutableStateFlow<UserState>(UserState.UnauthedUser)
    val authState = _authState.asStateFlow()

    private val _dialogState = MutableStateFlow<DialogState>(DialogState.Close)
    val dialogState = _dialogState.asStateFlow()

    fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            val userState = dashCoinUseCases.userStateProvider()
            _authState.update { userState }
            getMarketStatus(userState)
            getAllCoins(userState)
            updateFavoriteCoins(userState)
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
            updateFavoriteCoinsState(coins = result)
        }
    }

    private suspend fun getAllFavoriteCoinsPremium() {
        dashCoinUseCases.getAllFavoriteCoins().collect { result ->
            when (result) {
                is Resource.Loading -> updateFavoriteCoinsState(isLoading = true)
                is Resource.Success -> {
                    result.data?.let { data ->
                        updateFavoriteCoinsState(coins = data)
                    }
                }
                is Resource.Error -> updateFavoriteCoinsState(error = result.message)
            }
        }
    }

    private fun updateFavoriteCoins(
        user: UserState
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val coins = _state.value.coin
            if (coins.isEmpty()) {
                return@launch
            }

            coins.asyncMap { favoriteCoin ->
                val lastUpdated = favoriteCoin.lastUpdated
                if (isCurrentBiggerThanLastUpdated(lastUpdated) >= 5) {
                    val id = favoriteCoin.coinId
                    val result = dashCoinRepository.getCoinByIdRemote(id)
                    val coin = result.toFavoriteCoin().copy(lastUpdated = getCurrentDateTime())
                    dashCoinRepository.upsertFavoriteCoin(coin)
                }
            }


            getAllCoins(user)

        }
    }

    private fun isCurrentBiggerThanLastUpdated(lastUpdated: LocalDateTime): Int {
        val currentDate = getCurrentDateTime()
        val difference = currentDate.time.toMillisecondOfDay() - lastUpdated.time.toMillisecondOfDay()
        return difference / (60 * 1000)
    }

    private fun getMarketStatus(
        user: UserState
    ) {
        if (user == UserState.UnauthedUser) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            dashCoinRepository.getCoinByIdRemoteFlow("bitcoin").collect { result ->
                when (result) {
                    is Resource.Success -> {
                        delay(300)
                        _marketStatus.update {
                            it.copy(
                                coin = result.data,
                                loading = false,
                                error = ""
                            )
                        }
                    }
                    is Resource.Error -> {
                        _marketStatus.update {
                            MarketState(
                                error = result.message ?: "Unexpected Error",
                            )
                        }
                    }
                    is Resource.Loading -> {
                         _marketStatus.update {
                             MarketState(loading = true)
                         }
                    }
                }
            }
        }
    }

    fun refresh() {
        val userState = _authState.value
        updateFavoriteCoins(userState)
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

    companion object {
        private const val TAG = "FavoriteCoinsScreen"
    }
}