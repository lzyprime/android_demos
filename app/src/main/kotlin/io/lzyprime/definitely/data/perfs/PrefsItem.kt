package io.lzyprime.definitely.data.perfs

import kotlinx.coroutines.flow.Flow

abstract class PrefsItem<T>(delegate: Flow<T>) : Flow<T> by delegate {
    abstract suspend fun update(v: T)
}
