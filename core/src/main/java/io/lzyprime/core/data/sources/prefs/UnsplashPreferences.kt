package io.lzyprime.core.data.sources.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

interface UnsplashPreferences {
    val accessToken: PreferenceItem<String>
}

class DataStoreUnsplashPreferences(dataStore: DataStore<Preferences>) : UnsplashPreferences {

    companion object {
        private const val STORE_NAME = "unsplash"
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            STORE_NAME
        )
    }

    override val accessToken by dataStore(::stringPreferencesKey, "")
}