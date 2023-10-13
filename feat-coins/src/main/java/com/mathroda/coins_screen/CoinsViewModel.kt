package com.mathroda.coins_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.coins_screen.state.CoinsState
import com.mathroda.coins_screen.state.PaginationState
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.domain.model.Coins
import com.mathroda.internetconnectivity.InternetConnectivityManger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class CoinsViewModel @Inject constructor(
    private val dashCoinRepository: DashCoinRepository,
    val connectivityManger: InternetConnectivityManger
) : ViewModel() {

    private val _state = MutableStateFlow(CoinsState())
    val state = _state.asStateFlow()

    private val _paginationState = MutableStateFlow(PaginationState())
    val paginationState = _paginationState.asStateFlow()


    private val _isRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean> = _isRefresh


    init {
        if (_state.value.coins.isEmpty()) {
            getCoins(0)
        }
    }

    internal fun getCoins(
        skip: Int
    ) {
       dashCoinRepository.getCoinsRemote(skip = skip)
           .distinctUntilChanged()
           .onEach { result ->
                when (result) {
                    is Resource.Success -> result.data?.let { data -> onRequestSuccess(data) }
                    is Resource.Error -> onRequestError(result.message)
                    is Resource.Loading -> onRequestLoading()
                }
           }
           .launchIn(viewModelScope + SupervisorJob())
    }

    fun getCoinsPaginated() {
        if (_state.value.coins.isEmpty()) {
            return
        }

        if (_paginationState.value.endReached) {
            return
        }

        getCoins(_paginationState.value.skip)
    }

    internal fun onRequestSuccess(
        data: List<Coins>
    ) {
        _state.update {
            it.copy(
                coins = it.coins + data,
                isLoading = false,
                error = ""
            )
        }

        val listSize = _state.value.coins.size
        _paginationState.update {
            it.copy(
                skip = listSize,
                endReached = data.isEmpty() || listSize >= COINS_LIMIT,
                isLoading = false
            )
        }
    }

    internal fun onRequestError(
        message: String?
    ) {
        _state.update {
            it.copy(
                error = message ?: "Unexpected Error",
                isLoading = false,
            )
        }
    }
    internal fun onRequestLoading() {
        if (_state.value.coins.isEmpty()) {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
        }

        if (_state.value.coins.isNotEmpty()) {
            _paginationState.update {
                it.copy(
                    isLoading = true
                )
            }
        }
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            updateRefreshState(true)
            _paginationState.update { it.copy(skip = 0) }
            _state.update { it.copy(coins = emptyList()) }
            getCoins(0)
            updateRefreshState(false)
        }

    }
    private fun updateRefreshState(
        value: Boolean
    ) = _isRefresh.update { value }

    fun updateState(
        isLoading: Boolean = false,
        coins: List<Coins> = emptyList(),
        error: String = ""
    ) {
        _state.update {
            it.copy(
                isLoading = isLoading,
                coins = coins,
                error = error
            )
        }
    }

    companion object {
        const val COINS_LIMIT = 400
    }
}