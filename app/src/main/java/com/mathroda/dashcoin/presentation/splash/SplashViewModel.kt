package com.mathroda.dashcoin.presentation.splash

import androidx.compose.animation.fadeIn
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.use_case.DashCoinUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val dashCoinUseCases: DashCoinUseCases
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        getCoins()
    }

    private fun getCoins() =
        viewModelScope.launch {
            dashCoinUseCases.getCoins.invoke().collect { result ->
                when(result) {
                    is Resource.Loading -> { _isLoading.emit(true)}
                    is Resource.Success -> { _isLoading.emit(false)}
                    is Resource.Error -> { _isLoading.emit(false)}
                }
            }
        }



}