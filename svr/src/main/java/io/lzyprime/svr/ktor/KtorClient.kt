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
import io.lzyprime.svr.model.LoginState
import io.lzyprime.svr.model.StateAndEvent
import java.util.concurrent.TimeUnit

internal class KtorClient(tokenStorage: SvrService.TokenStorage) {
    companion object {
        private const val cookieName = "auth_code"
        private const val timeout = 5L // 5s
        private val timeUnit = TimeUnit.SECONDS // s
    }

    val stateAndEvent = StateAndEvent()

    private val cookiesStorage = object : CookiesStorage {
        private var cache: String? = null
        override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
            if (cookie.name == cookieName && cache != cookie.value) {
                tokenStorage.setToken(cookie.value)
                cache = cookie.value
            }
        }

        override suspend fun get(requestUrl: Url): List<Cookie> =
            when (val token = cache ?: tokenStorage.getToken().also { cache = it }) {
                "" -> emptyList()
                else -> listOf(Cookie(name = cookieName, value = token))
            }

        override fun close() {
            cache = null
        }
    }

    private val client by lazy {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json()
            }
            engine {
                config {
                    callTimeout(timeout, timeUnit)
                    addInterceptor {
                        val req = it.request()
                        println(req.url)
                        it.proceed(req)
                    }
                }
            }
            defaultRequest {
                url(SvrService.BASE_URL)
                contentType(ContentType.Application.Json)
            }
            install(HttpCookies) {
                storage = cookiesStorage
            }
        }
    }

    suspend fun isTokenExisted() = cookiesStorage.get(Url("")).isNotEmpty()

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
                Failed(rsp.status.value).let { f ->
                    if (f is Failed.TokenExpired) {
                        stateAndEvent.loginState.emit(LoginState.Logout(f))
                    }
                    Result.failure(f)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
}