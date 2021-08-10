package io.lzyprime.core.model.api

import io.lzyprime.core.model.bean.Photo
import retrofit2.http.GET

interface UnsplashService {
    @GET("photos")
    suspend fun listPhotos(): List<Photo>
}