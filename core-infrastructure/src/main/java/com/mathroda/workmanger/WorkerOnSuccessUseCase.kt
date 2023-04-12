package com.mathroda.workmanger

import com.mathroda.workmanger.repository.WorkerProviderRepository
import javax.inject.Inject

class WorkerOnSuccessUseCase @Inject constructor(
    private val workerProviderRepository: WorkerProviderRepository
) {
    operator fun invoke() = workerProviderRepository.onWorkerSuccess()
}