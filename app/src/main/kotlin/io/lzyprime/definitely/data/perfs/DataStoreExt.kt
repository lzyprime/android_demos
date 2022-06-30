package io.lzyprime.definitely.data.perfs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import kotlin.properties.ReadOnlyProperty

internal operator fun <T> DataStore<Preferences>.get(
    keyGen: (String) -> Preferences.Key<T>,
    def: T
) = ReadOnlyProperty<Any?, PrefsItem<T>> { _, property ->
    val key = keyGen("key_${property.name}")
    object : PrefsItem<T>(data.map { it[key] ?: def }) {
        override suspend fun update(v: T) {
            edit {
                it[key] = v
            }
        }
    }
}