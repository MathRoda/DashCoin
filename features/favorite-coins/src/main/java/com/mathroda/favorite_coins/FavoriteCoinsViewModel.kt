package com.mathroda.favorite_coins

import android.util.Log
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.mathroda.common.state.DialogState
import com.mathroda.common.state.MarketState
import com.mathroda.common.util.asyncMap
import com.mathroda.core.state.UserState
import com.mathroda.core.util.Resource
import com.mathroda.core.util.getCurrentDate
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.usecases.DashCoinUseCases
import com.mathroda.domain.model.FavoriteCoin
import com.mathroda.domain.model.toFavoriteCoin
import com.mathroda.favorite_coins.state.FavoriteCoinsState
import com.mathroda.phoneshaking.PhoneShakingManger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class FavoriteCoinsViewModel(
    private val dashCoinRepository: DashCoinRepository,
    private val dashCoinUseCases: DashCoinUseCases,
    val phoneShakingManger: PhoneShakingManger
) : ScreenModel {

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

        screenModelScope.launch(Dispatchers.IO) {
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
        screenModelScope.launch(Dispatchers.IO) {
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
        screenModelScope.launch(Dispatchers.IO) {
            val coins = _state.value.coin
            if (coins.isEmpty()) {
                return@launch
            }

            coins.asyncMap { favoriteCoin ->
                val lastUpdated = favoriteCoin.lastUpdated
                Log.d(TAG, "${favoriteCoin.coinId} was updated ${isCurrentBiggerThanLastUpdated(lastUpdated)} minutes")
                if (isCurrentBiggerThanLastUpdated(lastUpdated) >= 5) {
                    val id = favoriteCoin.coinId
                    val result = dashCoinRepository.getCoinByIdRemote(id)
                    val coin = result.toFavoriteCoin().copy(lastUpdated = getCurrentDate())
                    dashCoinRepository.upsertFavoriteCoin(coin)
                }
            }


            getAllCoins(user)

        }
    }

    private fun isCurrentBiggerThanLastUpdated(lastUpdated: Date): Long {
        val currentDate = getCurrentDate()
        val difference = currentDate.time - lastUpdated.time
        return difference / (60 * 1000)
    }

    private fun getMarketStatus(
        user: UserState
    ) {
        if (user == UserState.UnauthedUser) {
            return
        }

        screenModelScope.launch(Dispatchers.IO) {
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
        screenModelScope.launch(Dispatchers.IO) {
            dashCoinRepository.removeAllFavoriteCoins()
        }
    }

    companion object {
        private const val TAG = "FavoriteCoinsScreen"
    }
}