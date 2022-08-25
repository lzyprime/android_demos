package io.lzyprime.definitely.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lzyprime.definitely.data.UserRepository
import io.lzyprime.definitely.data.di.IODispatcher
import io.lzyprime.svr.model.Gender
import io.lzyprime.svr.model.LoginState
import io.lzyprime.svr.model.UserInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class NavUIState {
    SplashScreen,
    UpdateUserInfo,
    Main,
    Login,
}

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _navUIState = MutableStateFlow(NavUIState.SplashScreen)
    val navUIState = _navUIState.asStateFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            userRepository.autoLogin()
        }.invokeOnCompletion {
            viewModelScope.launch(ioDispatcher) {
                userRepository.loginState.filter { it != LoginState.Loading }
                    .collectLatest { loginState ->
                        _navUIState.emit(NavUIState.Login)
                        val state = if (loginState == LoginState.Logout) {
                            NavUIState.Login
                        } else {
                            val info = userRepository.userInfo.filterIsInstance<UserInfo>().first()
                            if (info.gender == Gender.Unknown || info.nickname.isBlank() || info.avatar.isBlank()) {
                                NavUIState.UpdateUserInfo
                            } else {
                                NavUIState.Main
                            }
                        }
                        _navUIState.emit(state)
                    }
            }
        }
    }
}