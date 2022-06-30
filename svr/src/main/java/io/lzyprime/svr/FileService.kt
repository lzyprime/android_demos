package io.lzyprime.svr

import io.lzyprime.svr.model.FileType

interface FileService {
    suspend fun putFile(type:FileType, byteArray: ByteArray):Result<Int>
}