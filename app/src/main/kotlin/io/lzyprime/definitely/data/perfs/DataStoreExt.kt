package io.lzyprime.definitely.data.perfs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KFunction1
import kotlin.reflect.KProperty

data class Item<T>(val flow: Flow<T>, val key:Preferences.Key<T>) : Flow<T> by flow

internal operator fun <T> DataStore<Preferences>.get(
    keyGen: (String) -> Preferences.Key<T>,
    def: T
) = ReadOnlyProperty<Any?, Item<T>> { ref, kProperty ->
    val key = keyGen("key_${ref}_${kProperty.name}")
    Item(data.map { it[key] ?: def }, key)
}

internal operator fun <T> KFunction1<String, Preferences.Key<T>>.getValue(
    ref: Any?,
    kProperty: KProperty<*>
) = this("key_${ref.toString()}_${kProperty.name}")
