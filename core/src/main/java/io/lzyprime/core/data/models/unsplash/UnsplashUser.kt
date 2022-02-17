package io.lzyprime.core.data.models.unsplash

data class UnsplashUser(
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

    val links: Links,
    val profile_image: ProfileImage,

) {
    data class Links(
        val followers: String,
        val following: String,
        val html: String,
        val likes: String,
        val photos: String,
        val portfolio: String,
        val self: String
    )

    data class ProfileImage(
        val large: String,
        val medium: String,
        val small: String,
    )
}