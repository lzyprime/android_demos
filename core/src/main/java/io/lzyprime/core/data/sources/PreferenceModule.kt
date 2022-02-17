package io.lzyprime.core.data.sources

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.lzyprime.core.data.modules.ApplicationScope
import io.lzyprime.core.data.prefs.DataStorePreferenceStorage
import io.lzyprime.core.data.prefs.DataStorePreferenceStorage.Companion.dataStore
import io.lzyprime.core.data.prefs.PreferenceStorage
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {

    @Provides
    fun providePreferenceStorage(
        @ApplicationContext context: Context
    ): PreferenceStorage = DataStorePreferenceStorage(context.dataStore)
}