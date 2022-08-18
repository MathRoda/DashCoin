package com.mathroda.dashcoin.domain.use_case

import com.mathroda.dashcoin.domain.use_case.auth.get_user.GetUserUseCase
import com.mathroda.dashcoin.domain.use_case.auth.signin.SignInUseCase
import com.mathroda.dashcoin.domain.use_case.auth.signout.SignOutUseCase
import com.mathroda.dashcoin.domain.use_case.auth.signup.IsCurrentUserUseCase
import com.mathroda.dashcoin.domain.use_case.auth.signup.SignUpUseCase

data class FirebaseUseCases(
    // domain/ use_case/ auth
    val signIn: SignInUseCase,
    val signOut: SignOutUseCase,
    val signUp: SignUpUseCase,
    val isCurrentUserExist: IsCurrentUserUseCase,
    val getCurrentUer: GetUserUseCase
)
