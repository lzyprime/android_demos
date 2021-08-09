package io.lzyprime.mvvmdemo.model.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.lzyprime.mvvmdemo.utils.Net
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    private inline fun <reified T> createService(): T = Net.retrofit.create(T::class.java)

    @Singleton
    @Provides
    fun unsplashService():UnsplashService = createService()
}