package io.lzyprime.core.domain

import io.lzyprime.core.data.UserRepository
import io.lzyprime.core.data.prefs.PreferenceStorage
import io.lzyprime.core.data.sources.UnsplashDataSource
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUnsplashUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(userCode: String): Boolean {
        return userRepository.loginUnsplash(userCode).onSuccess {
            UnsplashDataSource.token = it.access_token
            preferenceStorage.unsplashToken.update(it.access_token)
        }.isSuccess
    }

    // try auto login
    suspend operator fun invoke(): Boolean {
        val accessToken = preferenceStorage.unsplashToken.first()
        if(accessToken.isNotEmpty()) {
            UnsplashDataSource.token = accessToken
            return true
        }
        return false
    }
}