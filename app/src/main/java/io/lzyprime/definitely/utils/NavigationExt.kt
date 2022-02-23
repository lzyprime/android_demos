package io.lzyprime.definitely.utils

import androidx.navigation.NavController
import io.lzyprime.core.data.models.LoginState
import io.lzyprime.definitely.R

fun NavController.loginOrExec(loginState: LoginState, block:() -> Unit) {
    if(loginState == LoginState.Logged) {
        block()
    } else if(loginState is LoginState.NotLogin) {
        navigate(R.id.login_graph)
    }
}