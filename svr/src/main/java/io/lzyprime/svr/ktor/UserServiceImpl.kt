package io.lzyprime.svr.ktor

import io.ktor.client.request.*
import io.ktor.http.*
import io.lzyprime.svr.UserService
import io.lzyprime.svr.model.Failed
import io.lzyprime.svr.model.Gender
import io.lzyprime.svr.model.LoginState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

internal class UserServiceImpl(private val client: KtorClient) : UserService {
    private val _loginState get() = client.stateAndEvent.loginState
    override val loginState: SharedFlow<LoginState> = _loginState.asSharedFlow()

    override suspend fun login(user: String, password: String): Result<LoginState.LoginUserInfo> =
        client.doRequest<LoginState.LoginUserInfo> {
            _loginState.emit(LoginState.Loading)
            post("/user/login") {
                setBody(mapOf("user" to user, "password" to password))
            }
        }.onSuccess {
            _loginState.emit(it)
        }.onFailure {
            _loginState.emit(LoginState.Logout(it))
        }

    override suspend fun checkLogin() = if (client.isTokenExisted()) {
        client.doRequest<LoginState.LoginUserInfo> {
            _loginState.emit(LoginState.Loading)
            get("/user")
        }.onSuccess {
            _loginState.emit(it)
        }.onFailure {
            _loginState.emit(LoginState.Logout(it))
        }
    } else {
        _loginState.emit(LoginState.Logout())
        Result.failure(Failed.NotLogin)
    }

    override suspend fun updateAvatar(fileByteArray: ByteArray): Result<LoginState.LoginUserInfo> =
        client.doRequest<LoginState.LoginUserInfo> {
            put("/user/avatar") {
                contentType(ContentType.Image.Any)
                setBody(fileByteArray)
            }
        }.onSuccess {
            _loginState.emit(it)
        }
//    @kotlinx.serialization.Serializable
//    data class Req(val nickname: String, val gender: Gender)
    override suspend fun updateUserInfo(
        nickname: String,
        gender: Gender
    ): Result<LoginState.LoginUserInfo> =
        client.doRequest<LoginState.LoginUserInfo> {
            put("/user") {
                setBody(mapOf("nickname" to nickname, "gender" to gender.toString()))
//                setBody(Req(nickname, gender))
            }
        }.onSuccess {
            _loginState.emit(it)
        }
}