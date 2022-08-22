package io.lzyprime.svr.ktor

import io.ktor.client.request.*
import io.lzyprime.svr.UserService
import io.lzyprime.svr.model.*

internal class UserServiceImpl(private val ktorClient: KtorClient) : UserService {
    override val loginState get() = ktorClient.stateManager.loginState
    override val userInfoState = ktorClient.stateManager.userInfoState

    override suspend fun login(user: String, password: String) =
        ktorClient<UserInfoSvrModel> {
            post("/user/login") {
                setBody(mapOf("user" to user, "password" to password))
            }
        }.map {
            it.toUserInfo() to ktorClient.stateManager.token
        }

    override suspend fun login(token: String) =
        if (token.isBlank()) {
            Result.failure(Failed.TokenEmpty)
        } else if (ktorClient.stateManager.isEnableLogin()) {
            ktorClient.stateManager.token = token
            ktorClient<UserInfoSvrModel> {
                get("/user")
            }.map { it.toUserInfo() }.onSuccess {
                ktorClient.stateManager.updateUserInfo(it)
            }
        } else {
            Result.failure(Failed.AlreadyLogin)
        }

    override suspend fun updateUserInfo(
        nickname: String?,
        gender: Gender?,
        avatar: String?
    ) =
        ktorClient<UserInfoSvrModel> {
            put("/user") {
                setBody(
                    mapOf(
                        "nickname" to nickname,
                        "gender" to gender?.ordinal?.toString(),
                        "avatar" to avatar
                    )
                )
            }
        }.map { it.toUserInfo() }.onSuccess {
            ktorClient.stateManager.updateUserInfo(it)
        }
}