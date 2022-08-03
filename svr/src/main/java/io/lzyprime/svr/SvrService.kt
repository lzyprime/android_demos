package io.lzyprime.svr

import io.lzyprime.svr.ktor.SvrServiceImpl

interface SvrService {
    interface TokenStorage {
        suspend fun getToken(): String
        suspend fun setToken(newToken: String)
    }

    val userService: UserService
    val fileService: FileService

    companion object {
        internal const val BASE_URL = "http://10.81.31.15:8080"
        operator fun invoke(tokenStorage: TokenStorage): SvrService =
            SvrServiceImpl(tokenStorage)
    }
}



