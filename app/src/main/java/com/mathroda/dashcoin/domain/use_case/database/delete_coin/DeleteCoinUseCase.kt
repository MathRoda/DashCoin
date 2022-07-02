package com.mathroda.dashcoin.domain.use_case.database.delete_coin

import com.mathroda.dashcoin.domain.model.CoinById
import com.mathroda.dashcoin.domain.repository.DashCoinRepository
import javax.inject.Inject

class DeleteCoinUseCase @Inject constructor(
    private val repository: DashCoinRepository
) {

    suspend operator fun invoke(coins: CoinById) {
        repository.deleteCoin(coins)
    }
}