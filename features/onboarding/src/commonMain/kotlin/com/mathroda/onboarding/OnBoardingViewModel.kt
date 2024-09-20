package com.mathroda.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.datasource.datastore.DataStoreRepository
import com.mathroda.onboarding.utils.OnBoardingPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class OnBoardingViewModel(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val pager = listOf(
        OnBoardingPage.FirstScreen,
        OnBoardingPage.SecondScreen,
        OnBoardingPage.ThirdScreen,
    )

    fun saveOnBoardingState(completed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveOnBoardingState(completed)
        }
    }

}


