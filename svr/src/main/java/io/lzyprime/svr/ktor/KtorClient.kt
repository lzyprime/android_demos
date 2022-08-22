package io.lzyprime.svr.ktor

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.lzyprime.svr.SvrService
import io.lzyprime.svr.model.Failed
import io.lzyprime.svr.model.StateManager
import java.time.Duration

internal class KtorClient(reqTimeout: Duration?) {
    companion object {
        private val DefaultReqTimeout = Duration.ofSeconds(5) // 5s
    }



    val stateManager = StateManager()

    private val client by lazy {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json()
            }
            engine {
                config {
                    callTimeout(reqTimeout ?: DefaultReqTimeout)
                }
            }
            defaultRequest {
                url(SvrService.BASE_URL)
                contentType(ContentType.Application.Json)
            }
            install(HttpCookies) {
                storage = stateManager.cookieStorage
            }
        }
    }

    suspend inline operator fun <reified T> invoke(reqBlock: HttpClient.() -> HttpResponse): Result<T> =
        try {
            val rsp = client.reqBlock()
            if (rsp.status.isSuccess()) {
                try {
                    Result.success(rsp.body())
                } catch (_: Exception) {
                    Result.failure(Failed.ParseBodyFailed)
                }
            } else {
                Result.failure(Failed(rsp.status.value).also {
                    stateManager.onFailed(it)
                })
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
}