package com.mathroda.datasource.usecases

import com.google.common.truth.Truth.assertThat
import com.mathroda.core.state.UserState
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.datastore.DataStoreRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ProvideUserStateUseCaseTest {

    private lateinit var firebase: FirebaseRepository
    private lateinit var dataStore: DataStoreRepository
    private lateinit var dashCoin: DashCoinRepository
    private lateinit var useCase: ProvideUserStateUseCase

    @Before
    fun setUp() {
        clearAllMocks()
        firebase = mockk(relaxed = true)
        dataStore = mockk(relaxed = true)
        dashCoin = mockk(relaxed = true)
        useCase = ProvideUserStateUseCase(firebase, dataStore, dashCoin)
    }

    @Test
    fun `when user does not exist return UnauthedUser state`() = runTest {
        every { dataStore.readIsUserExistState } returns flowOf(false)
        every { firebase.isUserExist() } returns false

        val actual = useCase.invoke {  }.first()
        val expected = UserState.UnauthedUser

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `when user does exist but is not premium return AuthedUser state`() = runTest {
        every { dataStore.readIsUserExistState } returns flowOf(true)
        every { firebase.isUserExist() } returns true
        every { dashCoin.isUserPremiumLocal() } returns flowOf(false)

        val actual = useCase.invoke {  }.first()
        val expected = UserState.AuthedUser

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `when user does exist and he is premium return PremiumUser state`() = runTest {
        every { dataStore.readIsUserExistState } returns flowOf(true)
        every { firebase.isUserExist() } returns true
        every { dashCoin.isUserPremiumLocal() } returns flowOf(true)

        val actual = useCase.invoke {  }.first()
        val expected = UserState.PremiumUser

        assertThat(actual).isEqualTo(expected)
    }
}