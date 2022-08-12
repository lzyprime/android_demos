package io.lzyprime.svr.model

@kotlinx.serialization.Serializable
data class UserInfo(
    val uid: Int,
    val name: String,
    val nickname: String,
    val gender: Gender,
    val avatar: String,
)