package io.lzyprime.definitely.data

import io.lzyprime.svr.FileService
import io.lzyprime.svr.model.FileType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileRepository @Inject constructor(
    private val fileService: FileService,
) {
    private suspend fun putFile(type: FileType, byteArray: ByteArray) =
        fileService.putFile(type, byteArray)

    suspend fun putPicture(byteArray: ByteArray) =
        putFile(FileType.Picture, byteArray)
}