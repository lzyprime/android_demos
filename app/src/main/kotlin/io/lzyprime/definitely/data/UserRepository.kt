package io.lzyprime.definitely.data

import androidx.datastore.preferences.core.edit
import io.lzyprime.definitely.data.perfs.UserKey
import io.lzyprime.definitely.data.perfs.UserLocalDataSource
import io.lzyprime.svr.UserService
import io.lzyprime.svr.model.Gender
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val userService: UserService,
) {

    val loginState get() = userService.loginState
    val userInfo get() = userService.userInfoState

    suspend fun login(username: String, password: String) =
        userService.login(username, password).map { (res, token) ->
            userLocalDataSource.update(
                token,
                username,
            )
            res
        }

    suspend fun autoLogin() = userService.login(userLocalDataSource.svrToken.first())

    suspend fun updateUserInfo(nickname: String?, gender: Gender?, avatar: String?) =
        userService.updateUserInfo(nickname, gender, avatar)
}