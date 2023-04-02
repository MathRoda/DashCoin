package com.mathroda.coins_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.coins_screen.state.CoinsState
import com.mathroda.coins_screen.state.PaginationState
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.domain.Coins
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class CoinsViewModel @Inject constructor(
    private val dashCoinRepository: DashCoinRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CoinsState())
    val state = _state.asStateFlow()

    private val _paginationState = MutableStateFlow(PaginationState())
    val paginationState = _paginationState.asStateFlow()


    private val _isRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean> = _isRefresh


    init {
        if (_state.value.coins.isEmpty()) {
            getCoins()
        }
    }

    fun getCoins() {
       dashCoinRepository.getCoins(skip = _paginationState.value.skip)
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
        if (!_paginationState.value.endReached && _state.value.coins.isNotEmpty()) {
            if (_paginationState.value.isLoading) return
            getCoins()
        }
    }

    private fun onRequestSuccess(
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
                endReached = data.isEmpty() || listSize == COINS_LIMIT,
                isLoading = false
            )
        }
    }

    private fun onRequestError(
        message: String?
    ) {
        _state.update {
            it.copy(
                error = message ?: "Unexpected Error",
                isLoading = false,
            )
        }
    }
    private fun onRequestLoading() {
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
            getCoins()
            updateRefreshState(false)
        }

    }

    private fun updateRefreshState(
        value: Boolean
    ) = _isRefresh.update { value }

    companion object {
        const val COINS_LIMIT = 400
    }
}