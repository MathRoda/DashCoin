package com.mathroda.workmanger.repository

import androidx.work.*
import com.mathroda.core.state.UserState
import com.mathroda.core.util.Constants
import com.mathroda.datasource.providers.ProvidersRepository
import com.mathroda.workmanger.worker.DashCoinWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkerProviderRepositoryImpl @Inject constructor(
    private val workManager: WorkManager,
    private val scope: CoroutineScope,
    private val providerRepository: ProvidersRepository
) : WorkerProviderRepository {

    private val workConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()


    override fun createWork() {
        scope.launch {
            providerRepository.userStateProvider(function = {}).collect { state ->
                val workRequest = PeriodicWorkRequestBuilder<DashCoinWorker>(
                    repeatInterval = timeLong(state), repeatIntervalTimeUnit = timeUnit(state),
                    flexTimeInterval = timeLong(state), flexTimeIntervalUnit = timeUnit(state)
                ).setConstraints(workConstraints).setInitialDelay(15, TimeUnit.MINUTES)
                    .addTag(Constants.SYNC_DATA).build()

                workManager.enqueueUniquePeriodicWork(
                    Constants.SYNC_DATA_WORK_NAME,
                    ExistingPeriodicWorkPolicy.UPDATE,
                    workRequest
                )
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