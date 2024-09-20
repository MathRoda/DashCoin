package com.mathroda.workmanger

class CreateWorkUseCase (
    private val workerProviderRepository: WorkerProviderRepository
) {

    operator fun invoke() = workerProviderRepository.createWork()
}