package com.mathroda.dashcoin.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import com.mathroda.dashcoin.domain.use_case.DashCoinUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val dashCoinUseCases: DashCoinUseCases,
    private val firebaseRepository: FirebaseRepository
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        authUser()
    }

    private fun authUser() {
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
        }



}