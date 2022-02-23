package io.lzyprime.core.data

import android.util.Log
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

suspend inline fun <reified T> doRequest(
    context: CoroutineContext? = null,
    crossinline block: suspend () -> T
): Result<T> = try {
    Result.success(if (context != null) withContext(context) { block() } else block())
} catch (e: Throwable) {
    Log.e("request failed", e.stackTraceToString())
    Result.failure(e)
}
