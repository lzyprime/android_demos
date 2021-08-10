package io.lzyprime.core.viewmodel

import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lzyprime.core.model.DSManager
import io.lzyprime.core.model.SIG
import io.lzyprime.core.model.USER_ID
import io.lzyprime.core.model.UserRepository
import io.lzyprime.core.model.modules.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val DS: DSManager,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    val loginState get() = userRepository.loginState
    val userId get() = DS[USER_ID]

    fun login(userId: String, sig: String) = viewModelScope.launch {
        withContext(ioDispatcher) {
            if (userRepository.login(userId, sig)) {
                DS.edit {
                    it[USER_ID] = userId
                    it[SIG] = sig
                }
            }
        }
    }

    fun autoLogin() = viewModelScope.launch {
        withContext(ioDispatcher) {
            val userId = DS[USER_ID].first()
            val sig = DS[SIG].first()

            if (!userId.isNullOrEmpty() && !sig.isNullOrEmpty()) {
                login(userId, sig)
            }
        }
    }
}
