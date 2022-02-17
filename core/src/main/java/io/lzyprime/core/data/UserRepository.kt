package io.lzyprime.core.data

import io.lzyprime.core.data.sources.UnsplashDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val unsplashDataSource: UnsplashDataSource,
) {

    suspend fun loginUnsplash(userCode: String) =
        unsplashDataSource.login(userCode)

}