package io.lzyprime.mvvmdemo.model

import io.lzyprime.mvvmdemo.model.api.UnsplashService
import io.lzyprime.mvvmdemo.model.bean.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRepository @Inject constructor(private val service: UnsplashService) {
    suspend fun listPhoto() = try {
        Response.Success(service.listPhotos())
    } catch (_ : Exception){
        Response.Failed
    }
}