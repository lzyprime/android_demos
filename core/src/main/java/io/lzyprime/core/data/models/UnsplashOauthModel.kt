package io.lzyprime.core.data.models

data class UnsplashOauthModel(
    val access_token: String,
    val token_type: String,
    val scope: String,
    val created_at: Int,
)