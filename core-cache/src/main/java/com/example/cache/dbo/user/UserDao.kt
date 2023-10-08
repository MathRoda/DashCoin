package com.example.cache.dbo.user

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM User LIMIT 1")
    fun getUser(): UserEntity?

    @Query("SELECT COUNT (*) FROM User WHERE IsPremium = 1")
    fun isUserPremium(): Flow<Boolean>

    @Upsert
    suspend fun insertUser(user: UserEntity)

    @Query("DELETE FROM User")
    suspend fun deleteUser()
}