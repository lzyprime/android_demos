package io.lzyprime.core.data.sources.api

import io.lzyprime.core.data.models.UnsplashOauthModel
import io.lzyprime.core.data.models.UnsplashUserModel
import io.lzyprime.core.utils.UnsplashAccessKey
import io.lzyprime.core.utils.UnsplashSecretKey
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface UnsplashService {
    @POST("https://unsplash.com/oauth/token")
    suspend fun oauthToken(
        @Query("code") userCode: String,
        @Query("client_id") clientId: String = UnsplashAccessKey,
        @Query("client_secret") clientSecretKey: String = UnsplashSecretKey,
        @Query("redirect_uri") redirectUri: String = REDIRECT_URI,
        @Query("grant_type") grantType: String = GRANT_TYPE
    ): UnsplashOauthModel

    @GET("/me")
    suspend fun getUser(): UnsplashUserModel


    companion object {
        const val BASE_URL = "https://api.unsplash.com"
        const val REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob"
        const val GRANT_TYPE = "authorization_code"

        var token = ""

        private val retrofitClient by lazy {
            Retrofit.Builder()
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor {
                            val request = it.request().newBuilder()
                                .addHeader("Authorization", "Client-ID $UnsplashAccessKey")
                                .addHeader("Authorization", "Bearer $token")
                                .build()
                            it.proceed(request)
                        }.build()
                )
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }

        fun withRetrofit(): UnsplashService = retrofitClient.create(UnsplashService::class.java)
        fun withKtor(): UnsplashService = KtorUnsplashService()
    }
}

