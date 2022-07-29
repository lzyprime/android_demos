package io.lzyprime.definitely.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lzyprime.definitely.data.UserRepository
import io.lzyprime.definitely.data.models.stringResId
import io.lzyprime.svr.model.LoginState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _snackBarMsg = MutableSharedFlow<Int>()
    val snackBarMsg = _snackBarMsg.asSharedFlow()
    val loginState get() = userRepository.loginState

    init {
        viewModelScope.launch {
            userRepository.loginState.filterIsInstance<LoginState.Logout>().collectLatest {
                it.case.stringResId?.let { id -> _snackBarMsg.emit(id) }
            }
        }
    }

}