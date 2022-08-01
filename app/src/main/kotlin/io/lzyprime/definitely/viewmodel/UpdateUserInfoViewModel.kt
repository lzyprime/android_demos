package io.lzyprime.definitely.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lzyprime.definitely.data.UserRepository
import io.lzyprime.definitely.data.di.IODispatcher
import io.lzyprime.svr.model.Gender
import io.lzyprime.svr.model.LoginState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserInfoUiState(
    var avatar: String = "",
    var nickname: String = "",
    var gender: Gender = Gender.Unknown,
    var updatingUserInfo: Boolean = false,
    var updatingAvatar: Boolean = false,
) {
    val loading: Boolean get() = updatingAvatar || updatingUserInfo
    val submitButtonEnable: Boolean
        get() =
            avatar.isNotBlank() && nickname.isNotBlank() && gender != Gender.Unknown && !loading
}

@HiltViewModel
class UpdateUserInfoViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _userInfoUiState = MutableStateFlow(UserInfoUiState())
    val userInfoUiState = _userInfoUiState.asStateFlow()

    suspend fun initData(): UserInfoUiState {
        userRepository.loginState.firstOrNull()?.let { userInfo ->
            if (userInfo is LoginState.LoginUserInfo) {
                _userInfoUiState.update {
                    it.copy(
                        avatar = userInfo.avatar,
                        nickname = userInfo.nickname,
                        gender = userInfo.gender,
                    )
                }
            }
        }
        return _userInfoUiState.value
    }

    fun updateNickname(v: String) {
        _userInfoUiState.update { it.copy(nickname = v) }
    }

    fun updateGender(v: Gender) {
        _userInfoUiState.update { it.copy(gender = v) }
    }

    fun updateUserInfo() {
        val nickname = _userInfoUiState.value.nickname
        val gender = _userInfoUiState.value.gender
        if (nickname.isNotBlank() && gender != Gender.Unknown) {
            _userInfoUiState.update { it.copy(updatingUserInfo = true) }
            viewModelScope.launch(ioDispatcher) {
                userRepository.updateUserInfo(nickname, gender)
                _userInfoUiState.update { it.copy(updatingUserInfo = false) }
            }
        }
    }

    fun updateAvatar(byteArray: ByteArray) {
        _userInfoUiState.update { it.copy(updatingAvatar = true) }
        viewModelScope.launch(ioDispatcher) {
            userRepository.updateAvatar(byteArray).onSuccess { userInfo ->
                _userInfoUiState.update { it.copy(avatar = userInfo.avatar) }
            }
            _userInfoUiState.update { it.copy(updatingAvatar = false) }
        }
    }
}