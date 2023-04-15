package com.mathroda.workmanger.repository

import androidx.work.*
import com.mathroda.core.state.UserState
import com.mathroda.core.util.Constants
import com.mathroda.datasource.usecases.DashCoinUseCases
import com.mathroda.workmanger.worker.DashCoinWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkerProviderRepositoryImpl @Inject constructor(
    private val workManager: WorkManager,
    private val scope: CoroutineScope,
    private val dashCoinUseCases: DashCoinUseCases
) : WorkerProviderRepository {

    private val userState = MutableStateFlow(UserState.UnauthedUser)

    private val workConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()


    override fun createWork() {

        /**
         * Notification disabled until nex release
         */
        if (Constants.disableNotifications) {
            return
        }

        updateUserState()

        val workRequest = PeriodicWorkRequestBuilder<DashCoinWorker>(
            repeatInterval = timeLong(userState.value), repeatIntervalTimeUnit = timeUnit(userState.value),
            flexTimeInterval = timeLong(userState.value), flexTimeIntervalUnit = timeUnit(userState.value)
        ).setConstraints(workConstraints).setInitialDelay(15, TimeUnit.MINUTES)
            .addTag(Constants.SYNC_DATA).build()

        workManager.enqueueUniquePeriodicWork(
            Constants.SYNC_DATA_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }
    private fun updateUserState() {
        scope.launch {
            dashCoinUseCases.userStateProvider(function = {}).collect {
                userState.update { it }
            }
        }
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
}