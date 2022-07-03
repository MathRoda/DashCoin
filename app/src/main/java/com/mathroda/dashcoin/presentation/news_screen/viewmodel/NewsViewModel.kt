package com.mathroda.dashcoin.presentation.news_screen.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.use_case.DashCoinUseCases
import com.mathroda.dashcoin.presentation.news_screen.state.NewsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val dashCoinUseCases: DashCoinUseCases
): ViewModel() {


    private val _newsState = mutableStateOf(NewsState())
    val newsState: State<NewsState> = _newsState

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean> = _isRefresh


    init {
        getNews()
    }

    private fun getNews(filter: String = "trending") {
        dashCoinUseCases.getNews(filter).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _newsState.value = NewsState(news = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _newsState.value = NewsState(
                        error = result.message ?: "Unexpected Error"
                    )
                }
                is Resource.Loading -> {
                    _newsState.value = NewsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)

    }

    fun refresh() {
        viewModelScope.launch {
            _isRefresh.emit(true)
            getNews()
            _isRefresh.emit(false)
        }
    }

}



