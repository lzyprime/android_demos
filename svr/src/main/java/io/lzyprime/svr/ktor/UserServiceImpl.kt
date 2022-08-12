package io.lzyprime.svr.ktor

import io.ktor.client.request.*
import io.ktor.http.*
import io.lzyprime.svr.UserService
import io.lzyprime.svr.model.Failed
import io.lzyprime.svr.model.Gender
import io.lzyprime.svr.model.LoginState
import io.lzyprime.svr.model.UserInfo
import kotlinx.coroutines.flow.asStateFlow

internal class UserServiceImpl(private val client: KtorClient) : UserService {
    private val _loginState get() = client.stateAndEvent.loginState
    override val loginState get() = _loginState.asStateFlow()

    override suspend fun login(user: String, password: String) =
        client.doRequest<UserInfo> {
            post("/user/login") {
                setBody(mapOf("user" to user, "password" to password))
            }
        }.map {
            it to client.token
        }

    override suspend fun login(token: String) =
        if (token.isBlank()) {
            Result.failure(Failed.TokenEmpty)
        } else if (_loginState.compareAndSet(LoginState.Logout, LoginState.Loading)) {
            client.token = token
            client.doRequest<UserInfo> {
                get("/user")
            }.onSuccess {
                _loginState.emit(LoginState.LoggedIn)
            }.onFailure {
                _loginState.emit(LoginState.Logout)
            }
        } else {
            Result.failure(Failed.AlreadyLogin)
        }

    override suspend fun updateAvatar(fileByteArray: ByteArray) =
        client.doRequest<UserInfo> {
            put("/user/avatar") {
                contentType(ContentType.Image.Any)
                setBody(fileByteArray)
            }
        }

    override suspend fun updateUserInfo(nickname: String, gender: Gender) =
        client.doRequest<UserInfo> {
            put("/user") {
                setBody(mapOf("nickname" to nickname, "gender" to gender.toString()))
            }
        }
}