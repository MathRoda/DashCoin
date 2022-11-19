package com.mathroda.infrastructure.repository

import androidx.work.*
import com.mathroda.infrastructure.worker.DashCoinWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkerProviderRepositoryImpl @Inject constructor(
    private val workManager: WorkManager
) : WorkerProviderRepository {

    private val workConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()


    override fun createWork() {
        val workRequest = PeriodicWorkRequestBuilder<DashCoinWorker>(
            1, TimeUnit.DAYS,
            1, TimeUnit.DAYS
        ).setConstraints(workConstraints).setInitialDelay(15, TimeUnit.MINUTES)
            .addTag(com.mathroda.core.util.Constants.SYNC_DATA).build()

        workManager.enqueueUniquePeriodicWork(
            com.mathroda.core.util.Constants.SYNC_DATA_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    override fun onWorkerSuccess() =
        workManager.getWorkInfosByTagLiveData(com.mathroda.core.util.Constants.SYNC_DATA)
}