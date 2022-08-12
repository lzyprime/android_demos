package io.lzyprime.svr

import io.lzyprime.svr.ktor.KtorClient
import io.lzyprime.svr.ktor.SvrServiceImpl
import java.time.Duration

interface SvrService {
    val userService: UserService
    val fileService: FileService

    companion object {
        internal const val BASE_URL = "http://10.81.31.15:8080"
        operator fun invoke(
            reqTimeout: Duration? = null,
        ): SvrService =
            SvrServiceImpl(KtorClient(reqTimeout))
    }
}