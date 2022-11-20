package com.mathroda.domain

data class DashCoinUser(
    val userUid: String? = "",
    val userName: String? = "",
    val email: String? = "",
    val image: String? = "",
    val premium: Boolean = false,
    val favoriteCoinsCount: Int? = 0
) {
    fun isUserPremium(): Boolean {
        return premium
    }

    fun isPremiumLimit(): Boolean {
        return favoriteCoinsCount!! <= 3
    }

}
