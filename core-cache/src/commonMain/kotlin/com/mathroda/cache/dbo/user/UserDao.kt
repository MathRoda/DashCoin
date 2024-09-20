package com.mathroda.cache.dbo.user

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM User LIMIT 1")
    suspend fun getUser(): UserEntity?

    @Query("SELECT * FROM User LIMIT 1")
    fun getUserFlow(): Flow<UserEntity?>

    @Query("SELECT COUNT (*) FROM User WHERE IsPremium = 1")
    fun isUserPremium(): Flow<Boolean>

    @Upsert
    suspend fun insertUser(user: UserEntity)

    @Query("DELETE FROM User")
    suspend fun deleteUser()
}