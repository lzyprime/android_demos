package io.lzyprime.core.data.sources

import io.lzyprime.core.data.sources.prefs.UnsplashPreferences
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashLocalDataSource @Inject constructor(
    private val unsplashPreferences: UnsplashPreferences,
) {
    suspend fun updateToken(accessToken: String) {
        unsplashPreferences.accessToken.update(accessToken)
    }

    suspend fun getToken() = unsplashPreferences.accessToken.first()
}