package com.mathroda.news_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.domain.model.NewsType
import com.mathroda.internetconnectivity.InternetConnectivityManger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class NewsViewModel(
    private val dashCoinRepository: DashCoinRepository,
    val connectivityManger: InternetConnectivityManger
) : ViewModel() {


    private val _newsState = mutableStateOf(com.mathroda.news_screen.state.NewsState())
    val newsState: State<com.mathroda.news_screen.state.NewsState> = _newsState

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean> = _isRefresh

    private val defaultFilter = NewsFilter.TRENDING

    init {
        getNews(defaultFilter)
    }

    fun getNews(filter: NewsFilter) {
        dashCoinRepository.getNewsRemote(newsFilterConverter(filter)).onEach { result ->
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

    private fun newsFilterConverter(newsFilter: NewsFilter) =
        when (newsFilter) {
            NewsFilter.HANDPICKED -> NewsType.HANDPICKED
            NewsFilter.TRENDING -> NewsType.TRENDING
            NewsFilter.LATEST -> NewsType.LATEST
            NewsFilter.BULLISH -> NewsType.BULLISH
            NewsFilter.BEARISH -> NewsType.BEARISH
        }

    fun refresh() {
        viewModelScope.launch {
            _isRefresh.emit(true)
            getNews(defaultFilter)
            _isRefresh.emit(false)
        }
    }

}



