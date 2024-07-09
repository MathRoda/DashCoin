package com.mathroda.profile_screen.drawer.state

import com.mathroda.domain.model.FavoriteCoin

internal sealed class SyncState {
    data class NeedSync(val coins: List<FavoriteCoin>): SyncState()
    data object UpToDate: SyncState()
}
