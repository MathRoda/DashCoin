package com.mathroda.dashcoin.splash

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.mathroda.core.util.Constants.BITCOIN_ID
import com.mathroda.dashcoin.navigation.root.Graph
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.datastore.DataStoreRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.datasource.usecases.DashCoinUseCases
import com.mathroda.workmanger.repository.WorkerProviderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    firebaseRepository: FirebaseRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val dashCoinRepository: DashCoinRepository,
    private val dashCoinUseCases: DashCoinUseCases,
    workerProviderRepository: WorkerProviderRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _startDestination: MutableState<String> = mutableStateOf(Graph.ON_BOARDING)
    val startDestination: MutableState<String> = _startDestination

    private val isUserExist = firebaseRepository.isCurrentUserExist()

    private val onSuccessWorker = workerProviderRepository.onWorkerSuccess().value


    init {
        getOnBoardingState()
        cacheDashCoinUser()
        updateIsUserExistPref()
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

    private fun notificationWorker() {
        viewModelScope.launch {

            onSuccessWorker?.let { listOfWorkInfo ->

                if (listOfWorkInfo.isEmpty()) {
                    return@let
                }
                val workInfo: WorkInfo = listOfWorkInfo[0]

                if (workInfo.state == WorkInfo.State.ENQUEUED) {
                    dataStoreRepository.readIsUserExistState.collect { isUserExist ->
                        if (isUserExist) {
                            dashCoinRepository.getFlowFavoriteCoins().firstOrNull()
                        } else {
                            dashCoinRepository.getCoinByIdRemote(BITCOIN_ID).firstOrNull()
                        }
                    }
                }
            }
        }
    }

    private fun updateIsUserExistPref() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.readIsUserExistState.firstOrNull()?.let { doesExist ->
                Log.d("userDoesExist", doesExist.toString())
                if (doesExist) {
                    return@launch
                }
                isUserExist.collect { dataStoreRepository.saveIsUserExist(it) }
            }
        }
    }

    private fun cacheDashCoinUser() {
        viewModelScope.launch(Dispatchers.IO) {
            dashCoinUseCases.cacheDashCoinUser()
        }
    }
}