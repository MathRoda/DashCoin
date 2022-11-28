package com.mathroda.core.state

sealed class UserState {
    object AuthedUser: UserState()
    object UnauthedUser: UserState()
    object PremiumUser: UserState()
}
