package io.lzyprime.mvvmdemo.utils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Net {
    private const val PROP_BASE_URL = "https://api.unsplash.com/"
    private const val DEBUG_BASE_URL = "https://api.unsplash.com/"
    private val BASE_URL get() = DEBUG_BASE_URL
    var ACCESS_KEY: String = ""

    val retrofit: Retrofit by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val url =
                    chain.request().url().newBuilder().addQueryParameter("client_id", ACCESS_KEY)
                        .build()
                val request = chain.request().newBuilder().url(url).build()
                chain.proceed(request)
            }.build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
}