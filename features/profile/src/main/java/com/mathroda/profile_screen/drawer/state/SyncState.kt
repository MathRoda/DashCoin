package com.mathroda.profile_screen.drawer.state

import com.example.shared.FavoriteCoin

internal sealed class SyncState {
    data class NeedSync(val coins: List<com.example.shared.FavoriteCoin>): SyncState()
    data object UpToDate: SyncState()
}
