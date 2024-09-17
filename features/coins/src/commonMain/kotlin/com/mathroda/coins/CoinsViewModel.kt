package com.mathroda.coins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.coins.state.CoinsState
import com.mathroda.coins.state.PaginationState
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.domain.Coins
import com.mathroda.internetconnectivity.InternetConnectivityManger
import com.mathroda.internetconnectivity.InternetState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class CoinsViewModel(
    private val dashCoinRepository: DashCoinRepository,
    connectivityManger: InternetConnectivityManger
) : ViewModel() {

    private val _state = MutableStateFlow(CoinsState())
    val state = _state.asStateFlow()

    private val _paginationState = MutableStateFlow(PaginationState())
    val paginationState = _paginationState.asStateFlow()

    val connectivityMangerState = connectivityManger.getState()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = InternetState.IDLE
        )


    private val _isRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean> = _isRefresh


    init {
        if (_state.value.coins.isEmpty()) {
            getCoins()
        }
    }

    private fun getCoins(
        skip: Int = 1
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            dashCoinRepository.getCoinsRemote(skip = skip)
                .distinctUntilChanged()
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> result.data?.let { data -> onRequestSuccess(data) }
                        is Resource.Error -> onRequestError(result.message)
                        is Resource.Loading -> onRequestLoading()
                        else -> Unit
                    }
                }
        }
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

    private fun onRequestSuccess(
        data: List<Coins>
    ) {
        val coins = _state.value.coins + data
        _state.update {
            it.copy(
                coins = coins.toImmutableList(),
                isLoading = false,
                error = ""
            )
        }

        val listSize = _state.value.coins.size
        _paginationState.update {
            it.copy(
                skip = it.skip + 1,
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
            _state.update { it.copy(coins = persistentListOf()) }
            getCoins()
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
                coins = coins.toImmutableList(),
                error = error
            )
        }
    }

    companion object {
        const val COINS_LIMIT = 400
    }
}