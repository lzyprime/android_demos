package io.lzyprime.core.data.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.lzyprime.core.data.sources.prefs.DataStoreUnsplashPreferences
import io.lzyprime.core.data.sources.prefs.DataStoreUnsplashPreferences.Companion.dataStore
import io.lzyprime.core.data.sources.prefs.UnsplashPreferences

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {

    @Provides
    fun provideUnsplashPreferences(@ApplicationContext context: Context): UnsplashPreferences =
        DataStoreUnsplashPreferences(context.dataStore)
}