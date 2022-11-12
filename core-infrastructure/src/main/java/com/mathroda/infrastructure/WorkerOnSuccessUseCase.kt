package com.mathroda.infrastructure

import javax.inject.Inject

class WorkerOnSuccessUseCase @Inject constructor(
    private val workerProviderRepository: com.mathroda.infrastructure.repository.WorkerProviderRepository
) {
    operator fun invoke() = workerProviderRepository.onWorkerSuccess()
}