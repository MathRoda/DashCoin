package com.mathroda.workmanger

expect class WorkerProviderRepository {

    fun createWork()

    //fun onWorkerSuccess(): LiveData<List<WorkInfo>>

}