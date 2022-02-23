package io.lzyprime.core.data.sources

import io.lzyprime.core.data.doRequest
import io.lzyprime.core.data.models.UnsplashOauthModel
import io.lzyprime.core.data.models.UnsplashUserModel
import io.lzyprime.core.data.modules.IODispatcher
import io.lzyprime.core.data.sources.api.UnsplashService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRemoteDataSource @Inject constructor(
    private val unsplashService: UnsplashService,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    suspend fun login(userCode: String):Result<UnsplashOauthModel> = doRequest(ioDispatcher) {
        unsplashService.oauthToken(userCode)
    }

    fun setToken(token: String) {
        UnsplashService.token = token
    }

    suspend fun getUser():Result<UnsplashUserModel> = doRequest(ioDispatcher) {
        unsplashService.getUser()
    }
}
