package com.mathroda.infrastructure.repository

import androidx.lifecycle.LiveData
import androidx.work.WorkInfo

interface WorkerProviderRepository {

    fun createWork()

    fun onWorkerSuccess(): LiveData<List<WorkInfo>>

}