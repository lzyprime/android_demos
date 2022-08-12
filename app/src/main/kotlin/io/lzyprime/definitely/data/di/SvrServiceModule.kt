package io.lzyprime.definitely.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.lzyprime.definitely.data.perfs.UserLocalDataSource
import io.lzyprime.svr.FileService
import io.lzyprime.svr.SvrService
import io.lzyprime.svr.UserService
import kotlinx.coroutines.flow.first
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SvrServiceModule {
    private val svrService by lazy { SvrService() }

    @Provides
    fun provideUserService(): UserService = svrService.userService
}