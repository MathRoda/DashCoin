package com.mathroda.news_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.datasource.core.DashCoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val dashCoinRepository: DashCoinRepository,
) : ViewModel() {


    private val _newsState = mutableStateOf(com.mathroda.news_screen.state.NewsState())
    val newsState: State<com.mathroda.news_screen.state.NewsState> = _newsState

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean> = _isRefresh


    init {
        getNews()
    }

    private fun getNews(filter: String = "trending") {
        dashCoinRepository.getNews(filter).onEach { result ->
            when (result) {
                is com.mathroda.core.util.Resource.Success -> {
                    _newsState.value =
                        com.mathroda.news_screen.state.NewsState(news = result.data ?: emptyList())
                }
                is com.mathroda.core.util.Resource.Error -> {
                    _newsState.value = com.mathroda.news_screen.state.NewsState(
                        error = result.message ?: "Unexpected Error"
                    )
                }
                is com.mathroda.core.util.Resource.Loading -> {
                    _newsState.value = com.mathroda.news_screen.state.NewsState(isLoading = true)
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



