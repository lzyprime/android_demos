package io.lzyprime.svr.model

sealed interface LoginState {
    object Loading : LoginState
    private interface LoggedIn : LoginState
    data class Logout(val case: Throwable? = null) : LoginState

    @kotlinx.serialization.Serializable
    data class LoginUserInfo(
        val uid: Int,
        val name: String,
        val nickname: String,
        val gender: Gender,
        val avatar: String,
    ) : LoggedIn {
        val needComplete: Boolean
            get() = nickname.isEmpty() || avatar.isBlank() || gender == Gender.Unknown
    }
}
