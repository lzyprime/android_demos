package io.lzyprime.mvvmdemo.model

import io.lzyprime.mvvmdemo.model.api.UnsplashService
import io.lzyprime.mvvmdemo.model.bean.Res
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRepository @Inject constructor(private val service: UnsplashService) {
    suspend fun listPhoto() = try {
        Res.Success(service.listPhotos())
    } catch (e : Exception){
        Res.Failed(e)
    }
}