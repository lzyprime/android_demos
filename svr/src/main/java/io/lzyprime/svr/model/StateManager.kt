package io.lzyprime.svr.model

import io.ktor.client.plugins.cookies.*
import io.ktor.http.*
import io.lzyprime.svr.ktor.KtorClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class StateManager {
    private val _loginState = MutableStateFlow(LoginState.Logout)
    val loginState = _loginState.asStateFlow()
    private val _userInfoState = MutableStateFlow<UserInfoState>(UserInfo.None)
    val userInfoState = _userInfoState.asStateFlow()

    private data class CookieStorageWithToken(var token: String = "") : CookiesStorage {
        companion object {
            private const val COOKIE_NAME = "auth_code"
        }
        override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
            if (cookie.name == COOKIE_NAME && token != cookie.value) {
                token = cookie.value
            }
        }

        override fun close() {}

        override suspend fun get(requestUrl: Url): List<Cookie> =
            if (token.isBlank())
                emptyList()
            else
                listOf(Cookie(COOKIE_NAME, token))
    }

    private val cookieStorageWithToken = CookieStorageWithToken()
    val cookieStorage : CookiesStorage get() = cookieStorageWithToken

    var token
        get() = cookieStorageWithToken.token
        set(value) {
            cookieStorageWithToken.token = value
        }

    fun isEnableLogin(): Boolean =
        _userInfoState.value == UserInfo.None &&
                _loginState.compareAndSet(LoginState.Logout, LoginState.Loading)

    fun updateUserInfo(userInfo: UserInfo) {
        _loginState.compareAndSet(LoginState.Loading, LoginState.LoggedIn)
        _userInfoState.update { userInfo }
    }

    private fun logout() {
        _loginState.compareAndSet(LoginState.Loading, LoginState.Logout) || _loginState.compareAndSet(LoginState.LoggedIn, LoginState.Logout)
        _userInfoState.update { UserInfo.None }
    }

    fun onFailed(failed: Failed): Unit = when (failed) {
        Failed.TokenExpired -> logout()
        Failed.UserOrPasswordError -> logout()
        else -> {}
    }
}
