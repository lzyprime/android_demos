package io.lzyprime.core.model

import io.lzyprime.core.model.api.IMAuthService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val imService: IMAuthService,
) {
    val loginState get() = imService.loginState

    /** 登录IM. */
    suspend fun login(userId: String, sig: String): Boolean = imService.login(userId, sig)
}