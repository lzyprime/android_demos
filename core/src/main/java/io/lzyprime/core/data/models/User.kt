package io.lzyprime.core.data.models

data class User(
    val id: String,
    val username: String,
    val avatar: String,
    val email: String,
    val updateAt:String,

    val collections: Int,
    val likes: Int,
    val photos: Int,
    val followers: Int,
    val following: Int,

    val isFollowed: Boolean,

    val downloads: Int,
    val uploadsRemaining: Int,
)