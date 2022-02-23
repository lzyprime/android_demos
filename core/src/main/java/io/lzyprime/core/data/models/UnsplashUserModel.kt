package io.lzyprime.core.data.models

data class UnsplashUserModel(
    val id: String,
//    val uid: String, // id == uid
    val username: String,
    val name: String,
    val first_name: String,
    val last_name: String,
    val email: String,
    val updated_at: String,

    val total_collections: Int,
    val total_likes: Int,
    val total_photos: Int,

    val followed_by_user: Boolean,
    val followers_count: Int,
    val following_count: Int,

    val downloads: Int,
    val uploads_remaining: Int,

    val profile_image: ProfileImage,
) {

    data class ProfileImage(
        val large: String,
        val medium: String,
        val small: String,
    )
}