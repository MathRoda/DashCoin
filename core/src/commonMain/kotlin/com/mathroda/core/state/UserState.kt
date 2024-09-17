package com.mathroda.core.state

sealed class UserState {
    data object AuthedUser: UserState()
    data object UnauthedUser: UserState()
    data object PremiumUser: UserState()
}
