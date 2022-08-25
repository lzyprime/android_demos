package io.lzyprime.definitely.data.perfs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow


object UserKey {
    val SvrToken by ::stringPreferencesKey
    val Username by ::stringPreferencesKey
}

interface UserLocalDataSource {
    val svrToken: Flow<String>
    val username: Flow<String>

    suspend fun update(svrToken: String? = null, username: String? = null)
    companion object {
        fun withDataStore(context: Context) = UserLocalDataSourceImpl(context)
    }
}

class UserLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>
) : UserLocalDataSource {
    constructor(context: Context) : this(context.dataStore)

    companion object {
        private const val STORE_NAME = "definitely_user"
        private val Context.dataStore by preferencesDataStore(STORE_NAME)
    }

    override val svrToken by dataStore[::stringPreferencesKey, ""]
    override val username by dataStore[::stringPreferencesKey, ""]

    override suspend fun update(svrToken: String?, username: String?) {
        dataStore.edit {
            svrToken?.let { v -> it[this.svrToken.key] = v }
            username?.let { v -> it[this.username.key] = v }
        }
    }
}