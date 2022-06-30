package com.mathroda.dashcoin.presentation.news_screen.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.dashcoin.domain.use_case.DashCoinUseCases
import com.mathroda.dashcoin.presentation.news_screen.state.NewsState
import com.mathroda.dashcoin.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val dashCoinUseCases: DashCoinUseCases
): ViewModel() {

    private val _state = mutableStateOf(NewsState())
    val state: State<NewsState> = _state

    init {
        getNews()
    }


   private fun getNews(filter: String = "handpicked") {
        dashCoinUseCases.getNews(filter).onEach { result ->
            when(result) {
                is Resource.Success ->{
                    _state.value = NewsState(news = result.data?: emptyList())
                }
                is Resource.Error ->{
                    _state.value = NewsState(
                        error = result.message?: "Unexpected Error")
                }
                is Resource.Loading ->{
                    _state.value = NewsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}