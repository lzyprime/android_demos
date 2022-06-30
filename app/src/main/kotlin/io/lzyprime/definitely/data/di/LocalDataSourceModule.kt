package io.lzyprime.definitely.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.lzyprime.definitely.data.perfs.UserLocalDataSource

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModule {
    @Provides
    fun provideUserPrefsLocalDataSource(@ApplicationContext context: Context): UserLocalDataSource =
        UserLocalDataSource.withDataStore(context)
}