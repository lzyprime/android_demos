package io.lzyprime.core.data.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.lzyprime.core.data.sources.KtorUnsplashDataSource
import io.lzyprime.core.data.sources.UnsplashDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServerModule {

    @Provides
    @Singleton
    fun provideUnsplashSource(): UnsplashDataSource = KtorUnsplashDataSource() // or RetrofitUnsplashDataSource()
}