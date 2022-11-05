package com.mathroda.dashcoin.presentation.splash

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.utils.Utils.init
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.data.datastore.DataStoreRepository
import com.mathroda.dashcoin.domain.model.CoinById
import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import com.mathroda.dashcoin.domain.use_case.DashCoinUseCases
import com.mathroda.dashcoin.navigation.root.Graph
import com.mathroda.dashcoin.presentation.watchlist_screen.state.WatchListState
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
    private val dashCoinUseCases: DashCoinUseCases
): ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _startDestination:MutableState<String> = mutableStateOf(Graph.ON_BOARDING)
    val startDestination: MutableState<String> = _startDestination

    private val isUserExist = firebaseRepository.isCurrentUserExist()



    init {
        getOnBoardingState()
        getAllCoins()
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

    private fun getAllCoins() {
        isUserExist.onEach { exist ->
            if (exist) {
                firebaseRepository.getCoinFavorite().onEach { result ->
                    when (result) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            result.data?.map { coinById ->
                                dashCoinUseCases.getCoin.invoke(coinById.id ?: "").collect { result ->
                                    when (result) {
                                        is Resource.Loading -> {}
                                        is Resource.Success -> {
                                            firebaseRepository.addCoinFavorite(result.data ?: CoinById())
                                        }
                                        is Resource.Error -> {}
                                    }
                                }
                            }

                        }
                        is Resource.Error -> {}
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

}