package com.mathroda.domain

import kotlinx.serialization.Serializable

@Serializable
data class DashCoinUser(
    val userUid: String? = "",
    val userName: String? = "",
    val email: String? = "",
    val image: String? = "",
    val premium: Boolean = false,
    val favoriteCoinsCount: Int = 0
) {
    fun isUserPremium(): Boolean {
        return premium
    }

    fun isPremiumLimit(): Boolean {
        return favoriteCoinsCount >= FAVORITE_COINS_LIMIT
    }

    companion object {
        private const val FAVORITE_COINS_LIMIT = 400
    }

}
