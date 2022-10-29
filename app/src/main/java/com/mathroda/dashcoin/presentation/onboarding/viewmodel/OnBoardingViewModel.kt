package com.mathroda.dashcoin.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.dashcoin.data.datastore.DataStoreRepository
import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import com.mathroda.dashcoin.presentation.onboarding.utils.OnBoardingPage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) :ViewModel() {


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
}