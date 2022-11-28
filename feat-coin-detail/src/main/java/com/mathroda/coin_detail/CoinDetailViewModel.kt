package com.mathroda.coin_detail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.mathroda.domain.CoinById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
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
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _coinState = mutableStateOf(CoinState())
    val coinState: State<CoinState> = _coinState

    private val _chartState = mutableStateOf(ChartState())
    val chartState: State<ChartState> = _chartState

    private val _favoriteMsg = mutableStateOf("")
    val favoriteMsg: State<String> = _favoriteMsg

    private val _isFavoriteState = mutableStateOf<IsFavoriteState>(IsFavoriteState.NotFavorite)
    val isFavoriteState:State<IsFavoriteState> = _isFavoriteState

    private val _dialogState = mutableStateOf<DialogState>(DialogState.Close)
    val dialogState:MutableState<DialogState> = _dialogState

    private val _authState = mutableStateOf<UserState>(UserState.UnauthedUser)
    val authState:State<UserState> = _authState



    init {
        savedStateHandle.get<String>(PARAM_COIN_ID)?.let { coinId ->
            getChart(coinId)
            getCoin(coinId)
        }

    }

    private fun getCoin(coinId: String) {
        dashCoinRepository.getCoinById(coinId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _coinState.value = CoinState(coin = result.data)
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

    private fun getChart(coinId: String) {
        dashCoinRepository.getChartsData(coinId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _chartState.value =
                       ChartState(chart = result.data)
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

    fun onEvent(events: FavoriteCoinEvents) {
        when (events) {

            is FavoriteCoinEvents.AddCoin -> {
                viewModelScope.launch {
                    firebaseRepository.addCoinFavorite(events.coin).collect { result ->
                        when (result) {
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                _favoriteMsg.value = "${events.coin.name} successfully added to favorite! "
                                _isFavoriteState.value = IsFavoriteState.Favorite
                            }
                            is Resource.Error -> {
                                _favoriteMsg.value = result.message.toString()
                            }
                        }
                    }
                }
            }

            is FavoriteCoinEvents.DeleteCoin -> {
                viewModelScope.launch {
                    firebaseRepository.deleteCoinFavorite(events.coin).collect()
                    _isFavoriteState.value = IsFavoriteState.NotFavorite
                }
            }

        }
    }

    fun isFavorite(coin: CoinById) {
        viewModelScope.launch {
            firebaseRepository.isFavoriteState(coin).firstOrNull()?.let {
               if (coin.id == it.id) {
                   _isFavoriteState.value = IsFavoriteState.Favorite
               } else {
                   _isFavoriteState.value = IsFavoriteState.NotFavorite
               }
            }
        }
    }

    fun userState() {
        viewModelScope.launch {
            providersRepository.userStateProvider(
               function = {}
            ).collect {userState ->
                when(userState) {
                    is UserState.UnauthedUser -> _authState.value = userState
                    is UserState.AuthedUser -> _authState.value = userState
                    is UserState.PremiumUser -> _authState.value = userState
                }
            }
        }
    }

    private fun premiumLimit(coin: CoinById, sideEffect: MutableState<Boolean>) {
        viewModelScope.launch {
            firebaseRepository.getUserCredentials().collect { result ->
                result.data?.let { user ->
                    when(user.isPremiumLimit()) {
                        true -> onEvent(FavoriteCoinEvents.AddCoin(coin))
                        false -> sideEffect.value = true
                    }
                }
            }
        }
    }

    fun onFavoriteClick(
        coin: CoinById,
        sideEffect: MutableState<Boolean>,
    ) {
        when(_isFavoriteState.value) {
            is IsFavoriteState.Favorite -> {
                _dialogState.value = DialogState.Open
            }
            is IsFavoriteState.NotFavorite -> {
                when(_authState.value) {
                    is UserState.UnauthedUser -> sideEffect.value = true
                    is UserState.AuthedUser -> premiumLimit(coin, sideEffect)
                    is UserState.PremiumUser -> onEvent(FavoriteCoinEvents.AddCoin(coin))
                }
            }
        }
    }

}