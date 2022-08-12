package io.lzyprime.svr.model

import kotlinx.coroutines.flow.MutableStateFlow

internal class StateAndEvent {
    val loginState = MutableStateFlow(LoginState.Logout)
}
