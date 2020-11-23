package io.lzyprime.mvvmdemo.data.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.lzyprime.mvvmdemo.utils.Net
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ServiceModule {
    private inline fun <reified T> createService(): T = Net.retrofit.create(T::class.java)

    @Singleton
    @Provides
    fun unsplashService():UnsplashService = createService()
}