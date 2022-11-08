package com.mathroda.dashcoin.presentation.splash

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.data.datastore.DataStoreRepository
import com.mathroda.dashcoin.data.dto.toCoinDetail
import com.mathroda.dashcoin.domain.repository.DashCoinRepository
import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import com.mathroda.dashcoin.navigation.root.Graph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val dashCoinRepository: DashCoinRepository
): ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _startDestination:MutableState<String> = mutableStateOf(Graph.ON_BOARDING)
    val startDestination: MutableState<String> = _startDestination

    private val isUserExist = firebaseRepository.isCurrentUserExist()



    init {
        updateFavoriteCoinsStatus()
        getOnBoardingState()
    }

    private fun getOnBoardingState() {
        viewModelScope.launch {
            dataStoreRepository.readOnBoardingState.collect { completed ->
                if (completed) {
                    _startDestination.value = Graph.MAIN

                } else {
                    _startDestination.value = Graph.ON_BOARDING
                }
                delay(500)
                _isLoading.emit(false)
            }
        }
    }

    private fun updateFavoriteCoinsStatus() {
        firebaseRepository.getCoinFavorite().onEach { result ->
            when(result) {
                is Resource.Success -> {
                    result.data?.map {
                        dashCoinRepository.getCoinById(it.id ?: "").coin.toCoinDetail().let { coinById ->
                            firebaseRepository.updateFavoriteMarketState(coinById).collect { task ->
                                when(task) {
                                    is Resource.Success -> {
                                        Log.d("task---", it.priceChange1w.toString())
                                    }
                                    else -> {}
                                }
                            }
                        }
                    }
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

}