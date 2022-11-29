package com.mathroda.workmanger

import javax.inject.Inject

class CreateWorkUseCase @Inject constructor(
    private val workerProviderRepository: com.mathroda.workmanger.repository.WorkerProviderRepository
) {

    operator fun invoke() = workerProviderRepository.createWork()
}