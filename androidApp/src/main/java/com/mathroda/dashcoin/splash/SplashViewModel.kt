package com.mathroda.dashcoin.splash

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.core.destination.DashCoinDestinations
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.datastore.DataStoreRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.datasource.usecases.DashCoinUseCases
import com.mathroda.workmanger.WorkerProviderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(
    firebaseRepository: FirebaseRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val dashCoinRepository: DashCoinRepository,
    private val dashCoinUseCases: DashCoinUseCases,
    workerProviderRepository: WorkerProviderRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _startDestination: MutableState<DashCoinDestinations> = mutableStateOf(
        DashCoinDestinations.Onboarding)
    val startDestination: MutableState<DashCoinDestinations> = _startDestination

    private val isUserExist = firebaseRepository.isCurrentUserExist()

    //TODO: Remove Later
    //private val onSuccessWorker = workerProviderRepository.onWorkerSuccess().value

    private val userExist = MutableStateFlow(false)


    init {
        getOnBoardingState()
        updateIsUserExistPref()
        cacheDashCoinUser()
       // notificationWorker()
    }

    private fun getOnBoardingState() {
        viewModelScope.launch {
            dataStoreRepository.readOnBoardingState.collect { completed ->
                if (completed) {
                    _startDestination.value = DashCoinDestinations.Coins

                } else {
                    _startDestination.value = DashCoinDestinations.Onboarding
                }
                delay(500)
                _isLoading.emit(false)
            }
        }
    }

    /*private fun notificationWorker() {
        viewModelScope.launch(Dispatchers.IO) {
            onSuccessWorker?.let { listOfWorkInfo ->

                if (listOfWorkInfo.isEmpty()) {
                    return@launch
                }

                val workInfo: WorkInfo = listOfWorkInfo[0]

                if (workInfo.state == WorkInfo.State.ENQUEUED) {
                    if (userExist.value) {
                        dashCoinRepository.getFlowFavoriteCoins().firstOrNull()
                    } else {
                        dashCoinRepository.getCoinByIdRemote(BITCOIN_ID)
                    }
                }
            }
        }
    }*/

    private fun updateIsUserExistPref() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.readIsUserExistState.firstOrNull()?.let { doesExist ->
                Log.d("userDoesExist", doesExist.toString())
                if (doesExist) {
                    userExist.update { true }
                } else {
                    isUserExist.collect { dataStoreRepository.saveIsUserExist(it) }
                }
            }
        }
    }

    private fun cacheDashCoinUser() {
        if (userExist.value) {
            viewModelScope.launch(Dispatchers.IO) {
                dashCoinUseCases.cacheDashCoinUser()
            }
        }
    }
}