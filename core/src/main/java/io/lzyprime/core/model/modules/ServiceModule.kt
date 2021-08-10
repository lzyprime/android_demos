package io.lzyprime.core.model.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.lzyprime.core.model.api.UnsplashService
import io.lzyprime.core.utils.Net
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    private inline fun <reified T> createService(): T = Net.retrofit.create(T::class.java)

    @Singleton
    @Provides
    fun unsplashService(): UnsplashService = createService()
}