package com.mathroda.dashcoin.domain.use_case.worker

import com.mathroda.dashcoin.domain.repository.WorkerProviderRepository
import javax.inject.Inject

class WorkerOnSuccessUseCase @Inject constructor(
    private val workerProviderRepository: WorkerProviderRepository
) {
    operator fun invoke() = workerProviderRepository.onWorkerSuccess()
}