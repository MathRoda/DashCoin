package com.mathroda.dashcoin.data.databaes

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class DashCoinDatabaseTest {
    private lateinit var dashCoinDao: DashCoinDao
    private lateinit var dashCoinDatabase: DashCoinDatabase


    @Before
    fun setUp() {
            val context = ApplicationProvider.getApplicationContext<Context>()
            dashCoinDatabase = Room.inMemoryDatabaseBuilder(
                context,
                DashCoinDatabase::class.java
            ).allowMainThreadQueries().build()

        dashCoinDao = dashCoinDatabase.dashCoinDao
    }

    @After
    fun tearDown() {
        dashCoinDatabase.close()
    }

    @Test
    fun insertCoinToDatabase() = runTest {
        val coinById = FakeDataTest.coinById
        dashCoinDao.insertCoin(coinById)

        dashCoinDao.getAllCoins().onEach {
            assertThat(it).isEqualTo(coinById)
        }
    }

    @Test
    fun deleteCoinToDatabase() = runTest {
        val coinById = FakeDataTest.coinById
        dashCoinDao.insertCoin(coinById)
        dashCoinDao.deleteCoin(coinById)

        dashCoinDao.getAllCoins().onEach {
            assertThat(it).isEmpty()
        }
    }
}