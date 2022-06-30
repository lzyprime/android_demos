package io.lzyprime.svr.model

import kotlinx.coroutines.flow.MutableSharedFlow

internal class StateAndEvent {
    val loginState = MutableSharedFlow<LoginState>(replay = 1)
}
