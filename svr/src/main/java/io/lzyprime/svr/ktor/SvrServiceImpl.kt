package io.lzyprime.svr.ktor

import io.lzyprime.svr.FileService
import io.lzyprime.svr.SvrService
import io.lzyprime.svr.UserService

internal class SvrServiceImpl(tokenStorage: SvrService.TokenStorage) : SvrService {
    private val client = KtorClient(tokenStorage)

    override val userService: UserService by lazy {
        UserServiceImpl(client)
    }
    override val fileService: FileService by lazy {
        FileServiceImpl(client)
    }
}