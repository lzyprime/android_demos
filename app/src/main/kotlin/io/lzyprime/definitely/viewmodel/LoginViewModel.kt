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

data class LoginUiState(
    var username: String = "",
    var password: String = "",
    var isLoading : Boolean = false,
) {
    val enableLoginBtn: Boolean get() = username.isNotBlank() && password.isNotEmpty() && !isLoading
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    fun editUsername(text: CharSequence?) {
        _loginUiState.update { it.copy(username = text?.toString().orEmpty()) }
    }

    fun editPassword(text: CharSequence?) {
        _loginUiState.update { it.copy(password = text?.toString().orEmpty()) }
    }

    fun login() {
        _loginUiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(ioDispatcher) {
            val res = userRepository.login(
                _loginUiState.value.username,
                _loginUiState.value.password,
            ).isSuccess
            if(!res) {
                _loginUiState.update { it.copy(isLoading = false) }
            }
        }
    }
}