package io.lzyprime.definitely.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lzyprime.definitely.data.UserRepository
import io.lzyprime.definitely.data.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUIState(
    var username: String = "",
    var password: String = "",
    var isLoading: Boolean = false,
) {
    val enableLoginBtn: Boolean get() = username.isNotBlank() && password.isNotEmpty() && !isLoading
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _loginUIState = MutableStateFlow(LoginUIState())
    val loginUIState = _loginUIState.asStateFlow()

    fun editUsername(text: CharSequence?) {
        _loginUIState.update { it.copy(username = text?.toString().orEmpty()) }
    }

    fun editPassword(text: CharSequence?) {
        _loginUIState.update { it.copy(password = text?.toString().orEmpty()) }
    }

    fun login() {
        _loginUIState.update { it.copy(isLoading = true) }
        viewModelScope.launch(ioDispatcher) {
            userRepository.login(
                _loginUIState.value.username,
                _loginUIState.value.password,
            )
            _loginUIState.update { it.copy(isLoading = false) }
        }
    }
}