package io.lzyprime.core.data.models.unsplash

data class UnsplashOauth(
    val access_token: String,
    val token_type: String,
    val scope: String,
    val created_at: Int,
)