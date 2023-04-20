package com.mathroda.profile_screen.drawer.state

internal sealed class SyncState {
    object NeedSync: SyncState()
    object UpToDate: SyncState()
}
