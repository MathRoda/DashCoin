package com.mathroda.common.navigation

import android.net.Uri
import androidx.core.net.toUri
import com.mathroda.common.navigation.DestinationsDeepLink.HomePattern
import com.mathroda.domain.CoinById

object DestinationsDeepLink {

    private val BaseUri = "app://com.mathroda.dashcoin".toUri()

    val HomePattern = "$BaseUri/${Destinations.CoinsScreen.route}"
    val FavoriteCoinsPattern = "$BaseUri/${Destinations.FavoriteCoinsScreen.route}"

    fun getFavoriteCoinsUri(): Uri =
          FavoriteCoinsPattern.toUri()


    fun getHomeUri(): Uri =
        HomePattern.toUri()
}