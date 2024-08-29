package com.mathroda.cache.dbo.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mathroda.core.util.generateUUID
import com.mathroda.domain.DashCoinUser

@Entity(
    tableName = "User"
)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "UserUid") val userUid: String,
    @ColumnInfo(name = "UserName") val userName: String?,
    @ColumnInfo(name = "Email") val email: String?,
    @ColumnInfo(name = "Image") val image: String?,
    @ColumnInfo(name = "IsPremium") val isPremium: Boolean = false,
    @ColumnInfo(name = "CoinsCount") val coinsCount: Int = 0,
)

fun UserEntity.toDashCoinUser(): DashCoinUser {
    return DashCoinUser(
        userUid = userUid,
        userName = userName,
        email = email,
        image = image,
        premium = isPremium,
        favoriteCoinsCount = coinsCount
    )
}

fun DashCoinUser.toUserEntity(): UserEntity {
    return UserEntity(
        userUid = userUid ?: generateUUID(),
        userName = userName,
        email = email,
        image = image,
        isPremium = premium,
        coinsCount = favoriteCoinsCount
    )
}
