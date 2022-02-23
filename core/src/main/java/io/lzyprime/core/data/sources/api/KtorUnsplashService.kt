package io.lzyprime.core.data.sources.api

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.lzyprime.core.data.models.UnsplashOauthModel
import io.lzyprime.core.data.models.UnsplashUserModel
import io.lzyprime.core.utils.UnsplashAccessKey

class KtorUnsplashService : UnsplashService {
    companion object {
        private val ktorClient by lazy {
            HttpClient(OkHttp) {
                install(JsonFeature) {
                    serializer = GsonSerializer()
                }
                engine {
                    config {
                        addInterceptor {
                            val request = it.request().newBuilder()
                                .addHeader(
                                    HttpHeaders.Authorization,
                                    "Client-ID $UnsplashAccessKey"
                                )
                                .addHeader(
                                    HttpHeaders.Authorization,
                                    "Bearer ${UnsplashService.token}"
                                ).build()
                            it.proceed(request)
                        }
                    }
                }
            }
        }
    }

    override suspend fun oauthToken(
        userCode: String,
        clientId: String,
        clientSecretKey: String,
        redirectUri: String,
        grantType: String
    ): UnsplashOauthModel =
        ktorClient.post("https://unsplash.com/oauth/token") {
            mapOf("code" to userCode,
                    "client_id" to clientId,
                    "client_secret" to clientSecretKey,
                    "redirect_uri" to redirectUri,
                    "grant_type" to grantType,
            ).forEach(::parameter)
        }

    fun  withBaseUrl(url:String) = "${UnsplashService.BASE_URL}$url"

    override suspend fun getUser(): UnsplashUserModel =
        ktorClient.get(withBaseUrl("/me"))
}