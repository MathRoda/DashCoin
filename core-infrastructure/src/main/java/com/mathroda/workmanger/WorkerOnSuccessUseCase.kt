package com.mathroda.workmanger

import com.mathroda.workmanger.repository.WorkerProviderRepository

class WorkerOnSuccessUseCase (
    private val workerProviderRepository: WorkerProviderRepository
) {
    operator fun invoke() = workerProviderRepository.onWorkerSuccess()
}