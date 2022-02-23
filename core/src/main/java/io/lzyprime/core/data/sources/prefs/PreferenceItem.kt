package io.lzyprime.core.data.sources.prefs

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.properties.ReadOnlyProperty

abstract class PreferenceItem<T>(flow: Flow<T>) : Flow<T> by flow {
    abstract suspend fun update(v: T?)
}

operator fun <T> DataStore<Preferences>.invoke(
    buildKey: (name: String) -> Preferences.Key<T>,
    defaultValue: T,
) = ReadOnlyProperty<Any?, PreferenceItem<T>> { _, property ->
    val key = buildKey(property.name)
    object : PreferenceItem<T>(data.map { it[key] ?: defaultValue }) {
        override suspend fun update(v: T?) {
            Log.d("DataStore", "update {$key : $v}")
            edit {
                if (v == null) {
                    it -= key
                } else {
                    it[key] = v
                }
            }
        }
    }
}