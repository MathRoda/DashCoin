package com.mathroda.coins_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.coins_screen.state.CoinsState
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Collections.emptyList
import javax.inject.Inject

@HiltViewModel
class CoinsViewModel @Inject constructor(
    private val dashCoinRepository: DashCoinRepository,
    private val firebaseRepository: FirebaseRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CoinsState())
    val state: StateFlow<CoinsState> = _state

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean> = _isRefresh

    val isCurrentUserExist = firebaseRepository.isCurrentUserExist()


    init {
        getCoins()
    }


    private fun getCoins() {
        dashCoinRepository.getCoins().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.emit(
                        CoinsState(
                            coins = result.data ?: emptyList()
                        )
                    )
                }
                is Resource.Error -> {
                    _state.emit(
                        CoinsState(
                            error = result.message ?: "Unexpected Error"
                        )
                    )

                }
                is Resource.Loading -> {
                    _state.emit(
                        CoinsState(isLoading = true)
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefresh.emit(true)
            getCoins()
            _isRefresh.emit(false)
        }

    }

}