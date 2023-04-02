package com.mathroda.coin_detail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.mathroda.coin_detail.components.TimeRange
import com.mathroda.coin_detail.state.ChartState
import com.mathroda.coin_detail.state.CoinState
import com.mathroda.coin_detail.state.IsFavoriteState
import com.mathroda.common.events.FavoriteCoinEvents
import com.mathroda.common.state.DialogState
import com.mathroda.core.state.UserState
import com.mathroda.core.util.Constants.PARAM_COIN_ID
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.datasource.providers.ProvidersRepository
import com.mathroda.domain.ChartTimeSpan
import com.mathroda.domain.CoinById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val dashCoinRepository: DashCoinRepository,
    private val firebaseRepository: FirebaseRepository,
    private val providersRepository: ProvidersRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _coinState = mutableStateOf(CoinState())
    val coinState: State<CoinState> = _coinState

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

    init {
        updateUserState()
        updateUiState()
    }

    fun updateUiState() {
        savedStateHandle.get<String>(PARAM_COIN_ID)?.let { coinId ->
            getCoin(coinId)
            getChart(coinId, TimeRange.ONE_DAY)
        }
    }

    private fun getCoin(coinId: String) {
        dashCoinRepository.getCoinById(coinId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _coinState.value = CoinState(coin = result.data)
                    isFavorite()
                }
                is Resource.Error -> {
                    _coinState.value = CoinState(
                        error = result.message ?: "Unexpected Error"
                    )
                }
                is Resource.Loading -> {
                    _coinState.value = CoinState(isLoading = true)
                    delay(300)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getChart(coinId: String, period: TimeRange) {
        dashCoinRepository.getChartsData(coinId, getTimeSpanByTimeRange(period)).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    val chartsEntry = mutableListOf<Entry>()

                    result.data?.let { charts ->
                        charts.chart?.forEach { value ->
                            chartsEntry.add(addEntry(value[0], value[1]))
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
        }.launchIn(viewModelScope)
    }

    fun onTimeSpanChanged(
        timeRange: TimeRange
    ) {
       _coinState.value.coin?.id?.let { coinId ->
           getChart(
               coinId = coinId,
               period = timeRange
           )
       }
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
        when (events) {

            is FavoriteCoinEvents.AddCoin -> {
                viewModelScope.launch {
                    firebaseRepository.addCoinFavorite(events.coin).collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                _favoriteMsg.value = IsFavoriteState.Messages(
                                    favoriteMessage = "${events.coin.name} successfully added to favorite! "
                                )
                                _isFavoriteState.value = IsFavoriteState.Favorite
                            }
                            else-> {}
                        }
                    }
                }
            }

            is FavoriteCoinEvents.DeleteCoin -> {
                viewModelScope.launch {
                    firebaseRepository.deleteCoinFavorite(events.coin).collect { result ->
                        when(result) {
                            is Resource.Success -> {
                                _favoriteMsg.value = IsFavoriteState.Messages(
                                    notFavoriteMessage = "${events.coin.name} removed from favorite! "
                                )
                                _isFavoriteState.value = IsFavoriteState.NotFavorite
                            }
                            else -> {}
                        }
                    }
                }
            }

        }
    }

    private suspend fun isFavorite() {
        _coinState.value.coin?.let { coin ->
            firebaseRepository.isFavoriteState(coin).firstOrNull()?.let {
                if (coin.id == it.id) {
                    _isFavoriteState.value = IsFavoriteState.Favorite
                } else {
                    _isFavoriteState.value = IsFavoriteState.NotFavorite
                }
            }
        }
    }

    private fun updateUserState() {
        viewModelScope.launch {
            providersRepository.userStateProvider(
                function = {}
            ).collect { userState ->
                when (userState) {
                    is UserState.UnauthedUser -> _authState.value = userState
                    is UserState.AuthedUser -> _authState.value = userState
                    is UserState.PremiumUser -> _authState.value = userState
                }
            }
        }
    }

    private fun premiumLimit(coin: CoinById) {
        viewModelScope.launch {
            firebaseRepository.getUserCredentials().collect { result ->
                result.data?.let { user ->
                    when (user.isPremiumLimit()) {
                        true -> onEvent(FavoriteCoinEvents.AddCoin(coin))
                        false -> _notPremiumDialog.value = DialogState.Open
                    }
                }
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
                    is UserState.PremiumUser -> onEvent(FavoriteCoinEvents.AddCoin(coin))
                }
            }
        }
    }

    private fun addEntry(x: Float, y: Float) = Entry(x, y)
}