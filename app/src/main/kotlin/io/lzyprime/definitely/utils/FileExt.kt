package io.lzyprime.definitely.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

enum class MIMEType(val type: String, val path: String, val suffix: String?) {
    Pictures("image/*", "pictures", ".png")

}

fun Context.tempFile(mimeType: MIMEType, suffix: String? = null): Result<File> = kotlin.runCatching {
    File.createTempFile(mimeType.path, suffix ?: mimeType.suffix, cacheDir)
}

fun Context.fileContentUri(file: File): Uri = FileProvider.getUriForFile(this, packageName, file)