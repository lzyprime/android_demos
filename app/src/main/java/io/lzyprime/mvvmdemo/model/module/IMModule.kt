package io.lzyprime.mvvmdemo.model.module

import com.tencent.imsdk.v2.V2TIMManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object IMModule {

    @Singleton
    @Provides
    fun provideV2TIMManager(): V2TIMManager =
        V2TIMManager.getInstance()
}