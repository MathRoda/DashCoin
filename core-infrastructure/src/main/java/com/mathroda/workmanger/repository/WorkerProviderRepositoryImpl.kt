package com.mathroda.workmanger.repository

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mathroda.core.state.UserState
import com.mathroda.core.util.Constants
import com.mathroda.datasource.datastore.DataStoreRepository
import com.mathroda.datasource.usecases.DashCoinUseCases
import com.mathroda.workmanger.worker.DashCoinWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class WorkerProviderRepositoryImpl (
    private val workManager: WorkManager,
    private val scope: CoroutineScope,
    private val dashCoinUseCases: DashCoinUseCases,
    private val dataStoreRepository: DataStoreRepository
) : WorkerProviderRepository {

    private val userState: MutableStateFlow<UserState> = MutableStateFlow(UserState.UnauthedUser)

    private val workConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()


    override fun createWork() {
        scope.launch(Dispatchers.IO) {
            dataStoreRepository.readNotificationPreference.collect { enabled ->
                if (enabled) {
                    updateUserState()
                    createWorker()
                }

                if (!enabled) {
                    workManager.cancelUniqueWork(Constants.NOTIFICATION_WORKER)
                }
            }
        }
    }

    private fun createWorker() {
        val workRequest = PeriodicWorkRequestBuilder<DashCoinWorker>(
            repeatInterval = timeLong(userState.value),
            repeatIntervalTimeUnit = timeUnit(userState.value),
           // flexTimeInterval = timeLong(userState.value),
           // flexTimeIntervalUnit = timeUnit(userState.value)
        ).setConstraints(workConstraints)
            .setInitialDelay(30, TimeUnit.MINUTES)
            .addTag(Constants.SYNC_DATA)
            .build()

        workManager.enqueueUniquePeriodicWork(
            Constants.NOTIFICATION_WORKER,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    private suspend fun updateUserState() {
        userState.update { dashCoinUseCases.userStateProvider() }
    }

    override fun onWorkerSuccess() =
        workManager.getWorkInfosByTagLiveData(Constants.SYNC_DATA)

    /**
     * if user state -> Premium:
     * return 10 (to do work every 10 minutes)
     * -
     * if user state -> Authed:
     * return 1 (to do work every 1 day)
     *
     * if user state -> Unauthed:
     * return 1 (to do work every 1 day)
     */
    private fun timeLong(state: UserState): Long {
        return when(state) {
            is UserState.UnauthedUser -> 1
            is UserState.AuthedUser -> 1
            is UserState.PremiumUser -> 10
        }
    }

    /**
     * if user state -> Premium:
     * return Minutes (to do work every 10 minutes)
     * -
     * if user state -> Authed:
     * return Days (to do work every 1 day)
     *
     * if user state -> Unauthed:
     * return Days (to do work every 1 day)
     */
    private fun timeUnit(state: UserState): TimeUnit {
        return when(state) {
            is UserState.UnauthedUser -> TimeUnit.DAYS
            is UserState.AuthedUser -> TimeUnit.DAYS
            is UserState.PremiumUser -> TimeUnit.MINUTES
        }
    }

    companion object {
        const val TAG = "DebugDashCoin"
    }
}