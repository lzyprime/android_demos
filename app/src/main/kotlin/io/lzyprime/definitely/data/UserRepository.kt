package io.lzyprime.definitely.data

import io.lzyprime.definitely.data.di.ApplicationScope
import io.lzyprime.svr.UserService
import io.lzyprime.svr.model.Gender
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    @ApplicationScope private val scope: CoroutineScope,
    private val userService: UserService,
) {
    init {
        scope.launch { userService.checkLogin() }
    }

    val loginState get() = userService.loginState

    suspend fun login(user: String, password: String) =
        userService.login(user, password)

    suspend fun updateAvatar(fileByteArray: ByteArray) =
        userService.updateAvatar(fileByteArray)

    suspend fun updateUserInfo(nickname:String, gender: Gender) =
        userService.updateUserInfo(nickname, gender)
}