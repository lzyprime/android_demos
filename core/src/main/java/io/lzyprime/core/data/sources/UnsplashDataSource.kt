package io.lzyprime.core.data.sources

import io.ktor.client.*
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.lzyprime.core.data.models.unsplash.UnsplashOauth
import io.lzyprime.core.utils.UnsplashAccessKey
import io.lzyprime.core.utils.UnsplashSecretKey
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap


interface UnsplashDataSource {
    suspend fun login(userCode: String): Result<UnsplashOauth>

    companion object {
        const val BASE_URL = "https://api.unsplash.com"

        var token = ""
    }
}

class KtorUnsplashDataSource : UnsplashDataSource {
    companion object {
        private val ktorClient by lazy {
            HttpClient(OkHttp) {
                install(JsonFeature) {
                    serializer = GsonSerializer()
                }
                engine {
                    config {
                        addInterceptor {
                            val request = it.request().let { old ->
                                old.newBuilder()
                                    .url("${UnsplashDataSource.BASE_URL}${old.url}")
                                    .addHeader(
                                        HttpHeaders.Authorization,
                                        "Client-ID $UnsplashAccessKey"
                                    )
                                    .addHeader(
                                        HttpHeaders.Authorization,
                                        "Bearer ${UnsplashDataSource.token}"
                                    )
                            }.build()
                            it.proceed(request)
                        }
                    }
                }
            }
        }
    }

    override suspend fun login(userCode: String): Result<UnsplashOauth> = doRequest {
        ktorClient.post("/oauth/token") {
            mapOf(
                "client_secret" to UnsplashSecretKey,
                "code" to userCode
            ).forEach(::parameter)
        }
    }
}

private suspend inline fun <reified T> doRequest(
    crossinline block: suspend () -> T
): Result<T> = try {
    Result.success(block())
} catch (e: Exception) {
    Result.failure(e)
}

class RetrofitUnsplashDataSource(private val service: Service = Service()) : UnsplashDataSource {
    interface Service {
        @POST("/oauth/token")
        fun oauthToken(@QueryMap params: Map<String, Any?>): UnsplashOauth

        companion object {
            private val retrofitClient by lazy {
                Retrofit.Builder()
                    .client(
                        OkHttpClient.Builder()
                            .addInterceptor {
                                val request = it.request().newBuilder()
                                    .addHeader(
                                        HttpHeaders.Authorization,
                                        "Client-ID $UnsplashAccessKey"
                                    ).build()
                                it.proceed(request)
                            }.build()
                    )
                    .baseUrl(UnsplashDataSource.BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            }

            operator fun invoke(): Service = retrofitClient.create(Service::class.java)
        }
    }

    override suspend fun login(userCode: String): Result<UnsplashOauth> = doRequest {
        service.oauthToken(
            mapOf(
                "client_secret" to UnsplashSecretKey,
                "code" to userCode
            )
        )
    }
}