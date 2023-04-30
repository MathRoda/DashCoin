package com.mathroda.datasource.usecases

import com.google.common.truth.Truth.assertThat
import com.mathroda.core.state.IsFavoriteState
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.fake.DashCoinRepositoryFake
import com.mathroda.domain.model.FavoriteCoin
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class IsFavoriteStateUseCaseTest {
    private lateinit var dashCoin: DashCoinRepository
    private lateinit var useCase: IsFavoriteStateUseCase

    @Before
    fun setUp() {
        dashCoin = DashCoinRepositoryFake()
        useCase = IsFavoriteStateUseCase(dashCoin)
    }

    @Test
    fun `if coins is not found in Local DB return Not Favorite`() = runTest {
        val bitcoin = FavoriteCoin(coinId = "bitcoin")
        val ethereum = FavoriteCoin(coinId = "ethereum")
        dashCoin.addFavoriteCoin(ethereum)

        val actual = useCase.invoke(bitcoin)
        val expected = IsFavoriteState.NotFavorite

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `if coins is found in Local DB return Favorite`() = runTest {
        val bitcoin = FavoriteCoin(coinId = "bitcoin")
        dashCoin.addFavoriteCoin(bitcoin)

        val actual = useCase.invoke(bitcoin)
        val expected = IsFavoriteState.Favorite

        assertThat(actual).isEqualTo(expected)
    }
}