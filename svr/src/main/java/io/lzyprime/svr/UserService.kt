package io.lzyprime.svr

import io.lzyprime.svr.model.Gender
import io.lzyprime.svr.model.LoginState
import io.lzyprime.svr.model.UserInfo
import io.lzyprime.svr.model.UserInfoState
import kotlinx.coroutines.flow.StateFlow

interface UserService {
    val loginState: StateFlow<LoginState>
    val userInfoState: StateFlow<UserInfoState>

    suspend fun login(user: String, password: String): Result<Pair<UserInfo, String>>
    suspend fun login(token: String): Result<UserInfo>
    suspend fun updateUserInfo(
        nickname: String? = null,
        gender: Gender? = null,
        avatar: String? = null,
    ): Result<UserInfo>
}