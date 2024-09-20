package com.mathroda.news

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.domain.NewsType
import com.mathroda.internetconnectivity.InternetConnectivityManger
import com.mathroda.news.state.NewsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class NewsViewModel(
    private val dashCoinRepository: DashCoinRepository,
    val connectivityManger: InternetConnectivityManger
) : ViewModel() {

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
        }.launchIn(viewModelScope)

    }

    private fun newsFilterConverter(newsFilter: NewsFilter) =
        when (newsFilter) {
            NewsFilter.HANDPICKED ->NewsType.HANDPICKED
            NewsFilter.TRENDING ->NewsType.TRENDING
            NewsFilter.LATEST ->NewsType.LATEST
            NewsFilter.BULLISH ->NewsType.BULLISH
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



