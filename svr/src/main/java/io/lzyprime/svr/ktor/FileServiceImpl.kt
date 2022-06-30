package io.lzyprime.svr.ktor

import io.ktor.client.request.*
import io.lzyprime.svr.FileService
import io.lzyprime.svr.model.FileType

internal class FileServiceImpl(private val ktorClient: KtorClient) : FileService {
    override suspend fun putFile(type: FileType, byteArray: ByteArray): Result<Int> =
        ktorClient.doRequest {
            put(type.url) {
                setBody(byteArray)
            }
        }
}