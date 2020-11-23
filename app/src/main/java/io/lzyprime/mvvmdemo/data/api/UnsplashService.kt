package io.lzyprime.mvvmdemo.data.api

import io.lzyprime.mvvmdemo.data.bean.Photo
import retrofit2.http.GET

interface UnsplashService {
    @GET("photos")
    suspend fun listPhotos(): List<Photo>
}