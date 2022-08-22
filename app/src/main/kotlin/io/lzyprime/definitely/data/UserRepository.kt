package io.lzyprime.definitely.data

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

    suspend fun login(user: String, password: String) =
        userService.login(user, password).map { (res, token) ->
            userLocalDataSource.svrToken.update(token)
            res
        }
    suspend fun autoLogin() = userService.login(userLocalDataSource.svrToken.first())

    suspend fun updateUserInfo(nickname:String?, gender: Gender?, avatar:String?) =
        userService.updateUserInfo(nickname, gender, avatar)
}