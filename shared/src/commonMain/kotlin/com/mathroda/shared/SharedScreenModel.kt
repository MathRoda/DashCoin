package com.mathroda.shared

import com.mathroda.core.destination.DashCoinDestinations
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.datastore.DataStoreRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.datasource.usecases.DashCoinUseCases
import com.mathroda.workmanger.WorkerProviderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SharedScreenModel(
    firebaseRepository: FirebaseRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val dashCoinRepository: DashCoinRepository,
    private val dashCoinUseCases: DashCoinUseCases,
    workerProviderRepository: WorkerProviderRepository
) {
    private val screenModelScope =  CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var job: Job? = null

    private val isUserExist = firebaseRepository.isCurrentUserExist()

    //TODO: Remove Later
    //private val onSuccessWorker = workerProviderRepository.onWorkerSuccess().value

    private val userExist = MutableStateFlow(false)


    init {
        updateIsUserExistPref()
        cacheDashCoinUser()
        // notificationWorker()
    }

    val getOnBoardingState =
        dataStoreRepository.readOnBoardingState
            .map { completed ->
                if (completed) {
                    DashCoinDestinations.Coins

                } else {
                    DashCoinDestinations.Onboarding
                }
            }
            .stateIn(
                scope = screenModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = DashCoinDestinations.Onboarding
            )

    /*private fun notificationWorker() {
        screenModelScope.launch(Dispatchers.IO) {
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
        screenModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.readIsUserExistState.firstOrNull()?.let { doesExist ->
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
            screenModelScope.launch(Dispatchers.IO) {
                dashCoinUseCases.cacheDashCoinUser()
            }
        }
    }
}