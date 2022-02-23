package io.lzyprime.core.data.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.lzyprime.core.data.sources.api.UnsplashService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UnsplashModule {

    @Provides
    @Singleton
    fun provideUnsplashSource(): UnsplashService = UnsplashService.withKtor()
}