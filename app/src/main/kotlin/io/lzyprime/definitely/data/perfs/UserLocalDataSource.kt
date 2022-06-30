package io.lzyprime.definitely.data.perfs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore


interface UserLocalDataSource {
    val svrToken: PrefsItem<String>

    companion object {
        fun withDataStore(context: Context): UserLocalDataSource =
            DataStoreUserLocalDataSource(context)
    }
}

private class DataStoreUserLocalDataSource(
    dataStore: DataStore<Preferences>,
) : UserLocalDataSource {
    constructor(context: Context) : this(context.dataStore)

    companion object {
        private const val STORE_NAME = "definitely_user"
        private val Context.dataStore by preferencesDataStore(STORE_NAME)
    }

    override val svrToken by dataStore[::stringPreferencesKey, ""]
}