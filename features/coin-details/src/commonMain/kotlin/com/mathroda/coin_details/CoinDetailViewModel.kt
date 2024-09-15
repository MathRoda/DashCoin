package com.mathroda.coin_details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.coin_details.components.TimeRange
import com.mathroda.coin_details.state.ChartState
import com.mathroda.coin_details.state.CoinState
import com.mathroda.core.state.DialogState
import com.mathroda.core.state.IsFavoriteState
import com.mathroda.core.state.UserState
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.usecases.DashCoinUseCases
import com.mathroda.domain.ChartTimeSpan
import com.mathroda.domain.CoinById
import com.mathroda.domain.FavoriteCoin
import com.mathroda.domain.events.FavoriteCoinEvents
import com.mathroda.domain.toFavoriteCoin
import com.mathroda.internetconnectivity.InternetConnectivityManger
import com.mathroda.internetconnectivity.InternetState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CoinDetailViewModel(
    private val dashCoinRepository: DashCoinRepository,
    private val dashCoinUseCases: DashCoinUseCases,
    private val connectivityManger: InternetConnectivityManger
) : ViewModel() {

    private val _coinState = MutableStateFlow(CoinState())
    val coinState = _coinState.asStateFlow()

    private val _chartState = mutableStateOf(ChartState())
    val chartState: State<ChartState> = _chartState

    private val _favoriteMsg = mutableStateOf(IsFavoriteState.Messages())
    val favoriteMsg: State<IsFavoriteState.Messages> = _favoriteMsg

    private val _sideEffect = mutableStateOf(false)
    val sideEffect: State<Boolean> = _sideEffect

    private val _isFavoriteState = mutableStateOf<IsFavoriteState>(IsFavoriteState.NotFavorite)
    val isFavoriteState: State<IsFavoriteState> = _isFavoriteState

    private val _dialogState = mutableStateOf<DialogState>(DialogState.Close)
    val dialogState: MutableState<DialogState> = _dialogState

    private val _notPremiumDialog = mutableStateOf<DialogState>(DialogState.Close)
    val notPremiumDialog: MutableState<DialogState> = _notPremiumDialog

    private val _authState = mutableStateOf<UserState>(UserState.UnauthedUser)

    val connectivityMangerState: StateFlow<InternetState>
        get() = connectivityManger.getState()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = InternetState.IDLE
            )

    private var job: Job? = null

    fun updateUiState(coinId: String) {
        getCoin(coinId)
        getChart(coinId, TimeRange.ONE_DAY)
    }

    private fun getCoin(coinId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dashCoinRepository.getCoinByIdRemoteFlow(coinId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _coinState.update { CoinState(coin = result.data) }
                        result.data?.toFavoriteCoin()?.run { isFavorite(this) }
                    }
                    is Resource.Error -> {
                        _coinState.update {  CoinState(
                            error = result.message ?: "Unexpected Error"
                        ) }
                    }
                    is Resource.Loading -> {
                        _coinState.update { CoinState(isLoading = true) }
                        delay(300)
                    }
                }
            }
        }
    }

    private fun getChart(coinId: String, period: TimeRange) {
        job = viewModelScope.launch(Dispatchers.IO) {
            dashCoinRepository.getChartsDataRemote(coinId, getTimeSpanByTimeRange(period)).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        val chartsEntry = mutableListOf<Point>()

                        result.data?.let { charts ->
                            charts.chart.forEach { value ->
                                val x = value.timeStamp.toFloat()
                                val y = value.price.toFloat()
                                chartsEntry.add(Point(x, y))
                            }

                            _chartState.value = ChartState(chart = chartsEntry)
                        }
                    }
                    is Resource.Error -> {
                        _chartState.value = ChartState(
                            error = result.message ?: "Unexpected Error"
                        )
                    }
                    is Resource.Loading -> {
                        _chartState.value = ChartState(isLoading = true)
                    }
                }
            }
        }
    }

    fun onTimeSpanChanged(
        timeRange: TimeRange
    ) {
       _coinState.value.coin?.id?.let { coinId ->
           if (job != null) {
               job?.cancel()
           }

           getChart(
               coinId = coinId,
               period = timeRange
           )
       }
    }

    fun updateDialogState(
        state: DialogState
    ) {
        _dialogState.value = state
    }

    private fun getTimeSpanByTimeRange(timeRange: TimeRange): ChartTimeSpan =
        when (timeRange) {
            TimeRange.ONE_DAY -> ChartTimeSpan.TIMESPAN_1DAY
            TimeRange.ONE_WEEK -> ChartTimeSpan.TIMESPAN_1WEK
            TimeRange.ONE_MONTH -> ChartTimeSpan.TIMESPAN_1MONTH
            TimeRange.ONE_YEAR -> ChartTimeSpan.TIMESPAN_1YEAR
            TimeRange.ALL -> ChartTimeSpan.TIMESPAN_ALL
        }

    fun onEvent(events: FavoriteCoinEvents) {
        viewModelScope.launch(Dispatchers.IO) {
            when (events) {
                is FavoriteCoinEvents.AddCoin -> addFavoriteCoin(events.coin)
                is FavoriteCoinEvents.DeleteCoin -> deleteFavoriteCoin(events.coin)
            }
        }
    }

    private suspend fun addFavoriteCoin(coin: FavoriteCoin) {
        dashCoinRepository.upsertFavoriteCoin(coin)
        updateFavoriteCoinsCount()

        _isFavoriteState.value = IsFavoriteState.Favorite
        _favoriteMsg.value = IsFavoriteState.Messages(
            favoriteMessage = "${coin.name} successfully added to favorite!"
        )
    }

    private suspend fun deleteFavoriteCoin(coin: FavoriteCoin) {
        dashCoinRepository.removeFavoriteCoin(coin)
        updateFavoriteCoinsCount()

        _isFavoriteState.value = IsFavoriteState.NotFavorite
        _favoriteMsg.value = IsFavoriteState.Messages(
            notFavoriteMessage = "${coin.name} removed from favorite!"
        )
    }

    private fun updateFavoriteCoinsCount() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = dashCoinRepository.getDashCoinUser() ?: return@launch
            dashCoinRepository.getFlowFavoriteCoins().collect {
                val dashCoinUser = user.copy(favoriteCoinsCount = it.size )
                dashCoinRepository.cacheDashCoinUser(dashCoinUser)
            }
        }
    }

    private fun isFavorite(coin: FavoriteCoin) {
        viewModelScope.launch(Dispatchers.IO) {
            dashCoinUseCases.isFavoriteState(coin).let { result ->
                _isFavoriteState.value = result
            }
        }
    }

    fun updateUserState() {
        viewModelScope.launch (Dispatchers.IO){
            _authState.value =  dashCoinUseCases.userStateProvider()
        }
    }

    private fun premiumLimit(coin: CoinById) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = dashCoinRepository.getDashCoinUser() ?: return@launch
            if (user.isPremiumLimit()) {
                _notPremiumDialog.value = DialogState.Open
            } else {
                onEvent(FavoriteCoinEvents.AddCoin(coin.toFavoriteCoin()))
            }
        }
    }

    fun onFavoriteClick(
        coin: CoinById
    ) {
        when (_isFavoriteState.value) {
            is IsFavoriteState.Favorite -> {
                _dialogState.value = DialogState.Open
            }
            is IsFavoriteState.NotFavorite -> {
                when (_authState.value) {
                    is UserState.UnauthedUser -> _sideEffect.value = !_sideEffect.value
                    is UserState.AuthedUser -> premiumLimit(coin)
                    is UserState.PremiumUser -> onEvent(FavoriteCoinEvents.AddCoin(coin.toFavoriteCoin()))
                }
            }
        }
    }
}

data class Point(val x: Float, val y: Float)