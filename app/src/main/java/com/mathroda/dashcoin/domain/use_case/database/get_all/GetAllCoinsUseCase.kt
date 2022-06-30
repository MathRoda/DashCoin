package com.mathroda.dashcoin.domain.use_case.database.get_all

import com.mathroda.dashcoin.domain.model.CoinById
import com.mathroda.dashcoin.domain.repository.DashCoinRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCoinsUseCase @Inject constructor(
    private val repository: DashCoinRepository
) {

     operator fun invoke(): Flow<List<CoinById>> {
         return repository.getAllCoins()
     }
}