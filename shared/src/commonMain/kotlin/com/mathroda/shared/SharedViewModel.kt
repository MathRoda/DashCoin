package com.mathroda.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.datasource.datastore.DataStoreRepository
import com.mathroda.datasource.usecases.DashCoinUseCases
import com.mathroda.shared.destination.Destinations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedViewModel(
    private val dataStoreRepository: DataStoreRepository,
    private val dashCoinUseCases: DashCoinUseCases
): ViewModel() {

    private val _startDestination = MutableStateFlow<Destinations>(Destinations.CoinsScreen)
    val startDestinations = _startDestination.asStateFlow()

    init {
        getStartDestination()
        refreshUser()
    }

    private fun getStartDestination() {
        viewModelScope.launch(Dispatchers.IO) {
            val starDestinations = dataStoreRepository.readOnBoardingState
                .map { completed ->
                    if (completed) {
                        Destinations.CoinsScreen
                    } else {
                        Destinations.Onboarding
                    }
                }.first()

            withContext(Dispatchers.Main.immediate) {
                _startDestination.update { starDestinations }
            }
        }
    }
    private fun refreshUser() {
        viewModelScope.launch(Dispatchers.IO) {
            dashCoinUseCases.cacheDashCoinUser()
        }
    }

}