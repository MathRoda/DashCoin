package com.mathroda.dashcoin.infrastructure.worker

import androidx.work.*
import com.mathroda.dashcoin.core.util.Constants
import com.mathroda.dashcoin.domain.repository.WorkerProviderRepository
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkerProviderRepositoryImpl @Inject constructor(
    private val workManager: WorkManager
): WorkerProviderRepository {

    private val workConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .build()


    override fun createWork() {
        val workRequest = PeriodicWorkRequestBuilder<DashCoinWorker>(
            3, TimeUnit.SECONDS,
            3, TimeUnit.SECONDS
        ).setConstraints(workConstraints).setInitialDelay(3,TimeUnit.SECONDS)
            .addTag(Constants.SYNC_DATA).build()

        workManager.enqueueUniquePeriodicWork(
            Constants.SYNC_DATA_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    override fun onWorkerSuccess() = workManager.getWorkInfosByTagLiveData(Constants.SYNC_DATA)
}