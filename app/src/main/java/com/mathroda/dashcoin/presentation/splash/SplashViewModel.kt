package com.mathroda.dashcoin.presentation.splash

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.data.datastore.DataStoreRepository
import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import com.mathroda.dashcoin.domain.use_case.DashCoinUseCases
import com.mathroda.dashcoin.navigation.root.Graph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val dashCoinUseCases: DashCoinUseCases,
    private val firebaseRepository: FirebaseRepository,
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()


    init {
        getOnBoardingState()
        //authUser()
    }

    private fun getOnBoardingState() {
        viewModelScope.launch {
            dataStoreRepository.readOnBoardingState.collect() { completed ->
                if (completed) {
                   _isLoading.emit(false)
                } else {
                    _isLoading.emit(false)
                }

            }
        }
    }

     /*private fun authUser() {
        viewModelScope.launch {
           var isUserExist = false
           firebaseRepository.isCurrentUserExist().collect{
               isUserExist = it
           }

            if (isUserExist) {
                getCoins()
            } else _isLoading.emit(false)
        }

    }

    private fun getCoins() =
        viewModelScope.launch {
            dashCoinUseCases.getCoins.invoke().collect { result ->
                when(result) {
                    is Resource.Loading -> { _isLoading.emit(true)}
                    is Resource.Success -> { _isLoading.emit(false)}
                    is Resource.Error -> { _isLoading.emit(false)}
                }
            }
        }*/



}