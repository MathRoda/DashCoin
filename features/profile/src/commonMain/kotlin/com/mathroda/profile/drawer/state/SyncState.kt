package com.mathroda.profile.drawer.state

import com.mathroda.domain.FavoriteCoin

internal sealed class SyncState {
    data class NeedSync(val coins: List<FavoriteCoin>): SyncState()
    data object UpToDate: SyncState()
}
