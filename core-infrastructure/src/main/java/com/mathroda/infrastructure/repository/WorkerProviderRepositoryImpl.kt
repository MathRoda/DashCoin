package com.mathroda.infrastructure.repository

import androidx.work.*
import com.mathroda.core.util.Constants
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.infrastructure.worker.DashCoinWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkerProviderRepositoryImpl @Inject constructor(
    private val workManager: WorkManager,
    firebaseRepository: FirebaseRepository
) : WorkerProviderRepository {

    private val workConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    private val isUserExist = firebaseRepository.isUserExist()

    override fun createWork() {
        val workRequest = PeriodicWorkRequestBuilder<DashCoinWorker>(
            isUserExist.timeInteger(), isUserExist.timeUnit(),
            isUserExist.timeInteger(), isUserExist.timeUnit()
        ).setConstraints(workConstraints).setInitialDelay(5, TimeUnit.SECONDS)
            .addTag(Constants.SYNC_DATA).build()

        workManager.enqueueUniquePeriodicWork(
            Constants.SYNC_DATA_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    override fun onWorkerSuccess() =
        workManager.getWorkInfosByTagLiveData(Constants.SYNC_DATA)

    /**
     * if user exists:
     * return 10 (to do work every 10 minutes)
     * -
     * if user doesn't exists:
     * return 1 (to do work every 1 day)
     */
    private fun Boolean.timeInteger(): Long {
        return if (isUserExist) {
            10
        } else 1
    }

    /**
     * if user exists:
     * return Minutes (to do work every 10 minutes)
     * -
     * if user doesn't exists:
     * return Days (to do work every 1 day)
     */
    private fun Boolean.timeUnit(): TimeUnit {
        return if (isUserExist) {
            TimeUnit.MINUTES
        } else TimeUnit.DAYS
    }
}