package com.mathroda.coins_screen

import com.google.common.truth.Truth.assertThat
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.dispatcher.MainDispatcherRule
import com.mathroda.domain.model.Coins
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CoinsViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: DashCoinRepository
    private lateinit var viewModel: CoinsViewModel

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        viewModel = CoinsViewModel(repository)
    }

    /**
     * Testing onRequestLoading Functionality
     */

    @Test
    fun `onRequestLoading when coins state is empty update isLoading state to true`() = runTest {
        viewModel.updateState(coins = emptyList())
        viewModel.onRequestLoading()

        val isLoadingState = viewModel.state.value.isLoading
        val isLoadingPaginationState = viewModel.paginationState.value.isLoading

        assertThat(isLoadingState).isTrue()
        assertThat(isLoadingPaginationState).isFalse()
    }

    @Test
    fun `onRequestLoading when coins state is not empty update isLoading pagination state to true`() = runTest {
        viewModel.updateState(coins = listOfCoins)
        viewModel.onRequestLoading()

        val isLoadingState = viewModel.state.value.isLoading
        val isLoadingPaginationState = viewModel.paginationState.value.isLoading


        assertThat(isLoadingState).isFalse()
        assertThat(isLoadingPaginationState).isTrue()
    }

    /**
     * Testing onRequestError Functionality
     */

    @Test
    fun `when error message is not null update error state`() = runTest {
        val errorMsg = "Hi This is an Error"

        viewModel.onRequestError(errorMsg)

        val errorState = viewModel.state.value.error

        assertThat(errorState).isNotEmpty()
    }

    /**
     * Testing onRequestSuccess Functionality
     */

    @Test
    fun `onRequestSuccess update the coins list state`() = runTest {
        val testList = listOf(coins, coins)
        viewModel.updateState(coins = testList)

        viewModel.onRequestSuccess(listOfCoins)

        val coinsState = viewModel.state.value.coins
        val expectedList = testList + listOfCoins

        assertThat(coinsState).isNotEmpty()
        assertThat(coinsState).isEqualTo(expectedList)
    }

    @Test
    fun `onRequestSuccess update pagination state when data is not empty or didn't reached the limit`() = runTest {
        viewModel.onRequestSuccess(listOfCoins)

        val actualSkip = viewModel.paginationState.value.skip //4 coins
        val actualEndReached = viewModel.paginationState.value.endReached // false -> data is not empty OR didn't reach limit
        val actualIsLoadingState = viewModel.paginationState.value.isLoading // false

        assertThat(actualSkip).isEqualTo(listOfCoins.size)
        assertThat(actualEndReached).isFalse()
        assertThat(actualIsLoadingState).isFalse()
    }

    @Test
    fun `onRequestSuccess update pagination state when data is empty`() = runTest {
        viewModel.updateState(coins = listOfCoins)
        viewModel.onRequestSuccess(emptyList())

        val actualSkip = viewModel.paginationState.value.skip //4 coins
        val actualEndReached = viewModel.paginationState.value.endReached // true -> data is empty
        val actualIsLoadingState = viewModel.paginationState.value.isLoading // false

        assertThat(actualSkip).isEqualTo(listOfCoins.size)
        assertThat(actualEndReached).isTrue()
        assertThat(actualIsLoadingState).isFalse()
    }

    @Test
    fun `onRequestSuccess update pagination state when reached the limit`() = runTest {
        val testList = List(400) { index -> coins.copy(uniqueId = index.toString()) }

        viewModel.updateState(coins = testList)
        viewModel.onRequestSuccess(listOfCoins)

        val coinsState = viewModel.state.value.coins

        val actualSkip = viewModel.paginationState.value.skip //404 coins
        val actualEndReached = viewModel.paginationState.value.endReached // true -> limitReached
        val actualIsLoadingState = viewModel.paginationState.value.isLoading // false

        assertThat(actualSkip).isEqualTo(coinsState.size)
        assertThat(actualEndReached).isTrue()
        assertThat(actualIsLoadingState).isFalse()
    }

    /**
     * Testing getCoins Functionality
     */

    @Test
    fun `when getCoins called and result is Success`() = runTest {
        val skip = viewModel.paginationState.value.skip
        val data = List(10) { index -> coins.copy(uniqueId = index.toString()) }

        every { repository.getCoinsRemote(skip) } returns flowOf(Resource.Success(data))
        viewModel.getCoins(skip)

        val actualCoinsList = viewModel.state.value.coins
        val actualSkip = viewModel.paginationState.value.skip
        val actualEndReached = viewModel.paginationState.value.endReached
        val actualIsLoading = viewModel.paginationState.value.isLoading

        assertThat(actualCoinsList).isEqualTo(data)
        assertThat(actualSkip).isEqualTo(actualCoinsList.size)
        assertThat(actualEndReached).isFalse()
        assertThat(actualIsLoading).isFalse()
    }

    @Test
    fun `when getCoins called and result is Error`() = runTest {
        val skip = viewModel.paginationState.value.skip

        val errorMsg = "Error occurred"
        every { repository.getCoinsRemote(skip) } returns flowOf(Resource.Error(errorMsg))
        viewModel.getCoins(skip)

        val actualError = viewModel.state.value.error

        assertThat(actualError).isNotEmpty()
        assertThat(actualError).isEqualTo(errorMsg)
    }

    @Test
    fun `when getCoins called and result is Loading when coins state is empty`() = runTest {
        val skip = viewModel.paginationState.value.skip
        viewModel.updateState()

        every { repository.getCoinsRemote(skip) } returns flowOf(Resource.Loading())
        viewModel.getCoins(skip)

        val actualIsLoading = viewModel.state.value.isLoading
        val actualPaginationIsLoading = viewModel.paginationState.value.isLoading

        assertThat(actualIsLoading).isTrue()
        assertThat(actualPaginationIsLoading).isFalse()
    }

    @Test
    fun `when getCoins called and result is Loading when coins state is not empty`() = runTest {
        val skip = viewModel.paginationState.value.skip
        viewModel.updateState(coins = listOfCoins)

        every { repository.getCoinsRemote(skip) } returns flowOf(Resource.Loading())
        viewModel.getCoins(skip)

        val actualIsLoading = viewModel.state.value.isLoading
        val actualPaginationIsLoading = viewModel.paginationState.value.isLoading

        assertThat(actualIsLoading).isFalse()
        assertThat(actualPaginationIsLoading).isTrue()
    }

    companion object {
        private val coins = Coins(
            id = "bitcoin",
            icon = "Icon",
            marketCap = 100000.0,
            name = "Bitcoin",
            price = 24000.0,
            priceChange1d = 23999.0,
            rank = 1,
            symbol = "BTC"
        )

        //list of 4 coins
        val listOfCoins = listOf(
            coins, coins, coins, coins
        )
    }
}