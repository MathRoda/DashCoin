package com.mathroda.workmanger

import com.mathroda.workmanger.repository.WorkerProviderRepository
import javax.inject.Inject

class CreateWorkUseCase @Inject constructor(
    private val workerProviderRepository: WorkerProviderRepository
) {

    operator fun invoke() = workerProviderRepository.createWork()
}