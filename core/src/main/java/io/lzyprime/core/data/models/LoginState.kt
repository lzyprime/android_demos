package io.lzyprime.core.data.models

sealed class LoginState {
    object Loading : LoginState()
    object Logged: LoginState()
    sealed class NotLogin : LoginState() {
        companion object : NotLogin()
        object OauthTokenFailed : NotLogin()
    }
}
