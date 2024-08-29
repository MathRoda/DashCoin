package com.mathroda.workmanger

import com.mathroda.workmanger.WorkerProviderRepository

class CreateWorkUseCase (
    private val workerProviderRepository: WorkerProviderRepository
) {

    operator fun invoke() = workerProviderRepository.createWork()
}