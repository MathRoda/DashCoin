package com.mathroda.dashcoin.presentation.splash

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.dashcoin.data.datastore.DataStoreRepository
import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import com.mathroda.dashcoin.navigation.root.Graph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _startDestination:MutableState<String> = mutableStateOf(Graph.ON_BOARDING)
    val startDestination: MutableState<String> = _startDestination



    init {
        getOnBoardingState()
    }

    private fun getOnBoardingState() {
        viewModelScope.launch {
            dataStoreRepository.readOnBoardingState.collect { completed ->
                if (completed) {
                    _startDestination.value = Graph.MAIN

                } else {
                    _startDestination.value = Graph.ON_BOARDING
                }
                delay(500)
                _isLoading.emit(false)
            }
        }
    }

}