package io.lzyprime.core.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lzyprime.core.data.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    val loginState get() = userRepository.loginState
    val user get() = userRepository.user

    fun autoLogin() = viewModelScope.launch {
        userRepository.autoLogin()
    }

    fun login(userCode: String) {
        if (userCode.isNotEmpty()) {
            viewModelScope.launch {
                userRepository.login(userCode)
            }
        }
    }

    fun refreshUser() = viewModelScope.launch {
        userRepository.refreshUser()
    }
}