package com.mathroda.dashcoin.presentation.splash

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.mathroda.core.util.Constants.BITCOIN_ID
import com.mathroda.core.util.Resource
import com.mathroda.infrastructure.WorkerOnSuccessUseCase
import com.mathroda.dashcoin.navigation.root.Graph
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val dataStoreRepository: com.mathroda.datasource.datastore.DataStoreRepository,
    private val dashCoinRepository: DashCoinRepository,
    workerOnSuccessUseCase: com.mathroda.infrastructure.WorkerOnSuccessUseCase
): ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _startDestination:MutableState<String> = mutableStateOf(Graph.ON_BOARDING)
    val startDestination: MutableState<String> = _startDestination

    private val isUserExist = firebaseRepository.isCurrentUserExist()

    private val onSuccessWorker = workerOnSuccessUseCase.invoke().value



    init {
        updateFavoriteCoinsStatus()
        getOnBoardingState()
        notificationWorker()

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
                        dashCoinRepository.getCoinById(it.id ?: "").collect { result ->
                            when(result) {
                                is Resource.Success -> {
                                    result.data?.let { coinById ->
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
                                else -> {}
                            }

                        }
                    }
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun notificationWorker() {
        viewModelScope.launch {

            onSuccessWorker?.let { listOfWorkInfo ->

                if (listOfWorkInfo.isEmpty()) {
                    return@let
                }
                val workInfo: WorkInfo = listOfWorkInfo[0]

                if (workInfo.state == WorkInfo.State.ENQUEUED) {
                    dashCoinRepository.getCoinById(BITCOIN_ID).collect()
                }
            }
        }
    }

}