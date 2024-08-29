package com.mathroda.news_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.example.shared.NewsType
import com.mathroda.internetconnectivity.InternetConnectivityManger
import com.mathroda.news_screen.state.NewsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class NewsViewModel(
    private val dashCoinRepository: DashCoinRepository,
    val connectivityManger: InternetConnectivityManger
) : ScreenModel {

    private val _newsState = mutableStateOf(NewsState())
    val newsState: State<NewsState> = _newsState

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean> = _isRefresh

    private val defaultFilter = NewsFilter.TRENDING

    init {
        getNews(defaultFilter)
    }

    fun getNews(filter: NewsFilter) {
        dashCoinRepository.getNewsRemote(newsFilterConverter(filter)).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _newsState.value =
                        NewsState(news = result.data ?: emptyList())
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
        }.launchIn(screenModelScope)

    }

    private fun newsFilterConverter(newsFilter: NewsFilter) =
        when (newsFilter) {
            NewsFilter.HANDPICKED -> com.example.shared.NewsType.HANDPICKED
            NewsFilter.TRENDING -> com.example.shared.NewsType.TRENDING
            NewsFilter.LATEST -> com.example.shared.NewsType.LATEST
            NewsFilter.BULLISH -> com.example.shared.NewsType.BULLISH
            NewsFilter.BEARISH -> com.example.shared.NewsType.BEARISH
        }

    fun refresh() {
        screenModelScope.launch {
            _isRefresh.emit(true)
            getNews(defaultFilter)
            _isRefresh.emit(false)
        }
    }

}



