package io.lzyprime.svr

import io.lzyprime.svr.model.Gender
import io.lzyprime.svr.model.LoginState
import kotlinx.coroutines.flow.SharedFlow

interface UserService {
    val loginState: SharedFlow<LoginState>

    suspend fun login(user: String, password: String): Result<LoginState.LoginUserInfo>
    suspend fun checkLogin(): Result<LoginState.LoginUserInfo>
    suspend fun updateAvatar(fileByteArray: ByteArray): Result<LoginState.LoginUserInfo>
    suspend fun updateUserInfo(nickname:String, gender: Gender):Result<LoginState.LoginUserInfo>
}