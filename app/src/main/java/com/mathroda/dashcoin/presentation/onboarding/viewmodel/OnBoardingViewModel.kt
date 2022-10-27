package com.mathroda.dashcoin.presentation.onboarding.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.data.datastore.DataStoreRepository
import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import com.mathroda.dashcoin.navigation.root.Graph
import com.mathroda.dashcoin.presentation.onboarding.state.SignAnonymouslyState
import com.mathroda.dashcoin.presentation.onboarding.utils.OnBoardingPage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val firebaseRepository: FirebaseRepository
) :ViewModel() {


    private val _signAnonymously = MutableStateFlow(SignAnonymouslyState())
    val signAnonymously = _signAnonymously.asStateFlow()


    val pager = listOf(
        OnBoardingPage.FirstScreen,
        OnBoardingPage.SecondScreen,
        OnBoardingPage.ThirdScreen,
    )


    fun saveOnBoardingState(completed:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveOnBoardingState(completed)
        }
    }


     /* fun signAnonymously() {
        firebaseRepository.signInAnonymously().onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _signAnonymously.emit(SignAnonymouslyState(isLoading = true))
                }

                is Resource.Success -> {
                    _signAnonymously.emit(SignAnonymouslyState(success = result.data))
                }

                is Resource.Error -> {
                    _signAnonymously.emit(SignAnonymouslyState(error = result.message?: "Unexpected error accrued"))
                }
            }

        }.launchIn(viewModelScope)
    } */
}