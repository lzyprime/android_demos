package io.lzyprime.mvvmdemo.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val STORE_NAME = "global"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(STORE_NAME)
val USER_ID = stringPreferencesKey("user_id")
val SIG = stringPreferencesKey("sig")

suspend fun <T> DataStore<Preferences>.set(key: Preferences.Key<T>, value: T) = edit {
    it[key] = value
}

suspend fun <T> DataStore<Preferences>.set(key: Preferences.Key<T>, block: (oldValue: T?) -> T) =
    edit {
        it[key] = block(it[key])
    }

suspend fun <T> DataStore<Preferences>.get(key: Preferences.Key<T>): T? =
    this.data.map { it[key] }.first()

suspend fun <T> DataStore<Preferences>.get(key: Preferences.Key<T>, default: T): T =
    this.data.map { it[key] ?: default }.first()
