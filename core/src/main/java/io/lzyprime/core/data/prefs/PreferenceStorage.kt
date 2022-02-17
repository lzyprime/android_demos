package io.lzyprime.core.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.properties.ReadOnlyProperty

interface PreferenceStorage {
    abstract class Item<T>(flow: Flow<T>) : Flow<T> by flow {
        abstract suspend fun update(v: T?)
    }

    val unsplashToken: Item<String>
}

class DataStorePreferenceStorage(
    private val dataStore: DataStore<Preferences>
) : PreferenceStorage {

    companion object {
        private const val STORE_NAME = "global"
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            STORE_NAME
        )
    }

    private  fun <T> buildItem(
        buildKey: (name: String) -> Preferences.Key<T>,
        defaultValue: T,
    ) = ReadOnlyProperty<Any?, PreferenceStorage.Item<T>> { _, property ->
        val key = buildKey(property.name)
        object : PreferenceStorage.Item<T>(dataStore.data.map { it[key] ?: defaultValue }) {
            override suspend fun update(v: T?) {
                dataStore.edit {
                    if(v == null) {
                        it -= key
                    } else {
                        it[key] = v
                    }
                }
            }
        }
    }

    override val unsplashToken by buildItem(::stringPreferencesKey, "")
}