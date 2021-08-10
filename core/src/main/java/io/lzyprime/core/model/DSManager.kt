package io.lzyprime.core.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val USER_ID = stringPreferencesKey("user_id")
val SIG = stringPreferencesKey("sig")

@Singleton
class DSManager @Inject constructor(@ApplicationContext context: Context)
    :DataStore<Preferences> by context.dataStore {

    operator fun <T> get(key: Preferences.Key<T>): Flow<T?> =
        data.map { it[key] }

    companion object {
        private const val STORE_NAME = "global"
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(STORE_NAME)
    }
}
