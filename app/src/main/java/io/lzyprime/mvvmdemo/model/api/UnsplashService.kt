package io.lzyprime.mvvmdemo.model.api

import io.lzyprime.mvvmdemo.model.bean.Photo
import retrofit2.http.GET

interface UnsplashService {
    @GET("photos")
    suspend fun listPhotos(): List<Photo>
}