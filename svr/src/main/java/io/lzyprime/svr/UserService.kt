package io.lzyprime.svr

import io.lzyprime.svr.model.Gender
import io.lzyprime.svr.model.LoginState
import io.lzyprime.svr.model.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UserService {
    val loginState: StateFlow<LoginState>

    suspend fun login(user: String, password: String): Result<Pair<UserInfo, String>>
    suspend fun login(token: String): Result<UserInfo>
    suspend fun updateAvatar(fileByteArray: ByteArray): Result<UserInfo>
    suspend fun updateUserInfo(nickname: String, gender: Gender): Result<UserInfo>
}