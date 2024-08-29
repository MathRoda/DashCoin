package com.mathroda.cache.dbo.user

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mathroda.cache.DashCoinDatabase
import com.mathroda.domain.DashCoinUser
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CacheUserTest {
    private lateinit var dao: UserDao
    private lateinit var db: DashCoinDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            DashCoinDatabase::class.java
        ).build()
        dao = db.userDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeAndReadUserFromDatabase() = runBlocking {
        val user = DashCoinUser(
            userUid = "testUser",
            userName = "mathroda",
            email = "mathroda@dashcoin.com"
        )

        dao.insertUser(user.toUserEntity())
        val cachedUser = dao.getUser()

        assert(cachedUser != null)
        assert(cachedUser == user.toUserEntity())
    }
}