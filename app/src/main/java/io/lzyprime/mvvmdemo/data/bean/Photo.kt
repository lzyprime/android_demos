package io.lzyprime.mvvmdemo.data.bean

data class Photo(
    val id: String,
    val created_at: String = "",
    val updated_at: String = "",
    val urls: Urls,
    val likes: Long = 0,
    val liked_by_user: Boolean = false,
    val user: User,
) {
    data class Urls(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String,
    )
}