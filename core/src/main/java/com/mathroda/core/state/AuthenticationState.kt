package com.mathroda.core.state

sealed class AuthenticationState {
    object AuthedUser: AuthenticationState()
    object UnauthedUser: AuthenticationState()
}
