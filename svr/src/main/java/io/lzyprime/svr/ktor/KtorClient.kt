package io.lzyprime.svr.ktor

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.lzyprime.svr.SvrService
import io.lzyprime.svr.model.Failed
import io.lzyprime.svr.model.StateAndEvent
import java.time.Duration

internal class KtorClient(reqTimeout: Duration?) {
    companion object {
        private val DefaultReqTimeout = Duration.ofSeconds(5) // 5s
        private const val COOKIE_NAME = "auth_code"
    }

    private data class CookieStorageWithToken(var token: String = "") : CookiesStorage {
        override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
            if (cookie.name == COOKIE_NAME && token != cookie.value) {
                token = cookie.value
            }
        }

        override fun close() {}

        override suspend fun get(requestUrl: Url): List<Cookie> =
            if (token.isBlank())
                emptyList()
            else
                listOf(Cookie(COOKIE_NAME, token))
    }

    private val cookieStorageWithToken = CookieStorageWithToken()
    var token get() = cookieStorageWithToken.token
    set(value) {
        cookieStorageWithToken.token = token
    }

    val stateAndEvent = StateAndEvent()

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
                storage = cookieStorageWithToken
            }
        }
    }

    suspend inline fun <reified T> doRequest(reqBlock: HttpClient.() -> HttpResponse): Result<T> =
        try {
            val rsp = client.reqBlock()
            if (rsp.status.isSuccess()) {
                try {
                    Result.success(rsp.body())
                } catch (_: Exception) {
                    Result.failure(Failed.ParseBodyFailed)
                }
            } else {
                Result.failure(Failed(rsp.status.value))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
}