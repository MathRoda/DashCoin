package com.mathroda.dashcoin.domain.repository

import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import kotlinx.coroutines.flow.Flow

interface WorkerProviderRepository {

    fun createWork()

    fun onWorkerSuccess(): LiveData<List<WorkInfo>>
}