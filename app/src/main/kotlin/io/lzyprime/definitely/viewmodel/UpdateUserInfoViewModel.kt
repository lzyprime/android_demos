package io.lzyprime.definitely.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lzyprime.definitely.R
import io.lzyprime.definitely.data.FileRepository
import io.lzyprime.definitely.data.UserRepository
import io.lzyprime.definitely.data.di.IODispatcher
import io.lzyprime.svr.model.Gender
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserInfoUiState(
    var avatar: String = "",
    var nickname: String = "",
    var gender: Gender = Gender.Unknown,
    var loading: Boolean = true
) {
    val editEnable: Boolean get() = !loading
    val submitButtonEnable: Boolean
        get() =
            avatar.isNotBlank() && nickname.isNotBlank() && gender != Gender.Unknown && !loading
}

sealed interface UserInfoAction {
    @JvmInline
    value class UpdateNickname(val nickname: String) : UserInfoAction

    @JvmInline
    value class UpdateGender(val gender: Gender) : UserInfoAction

    @JvmInline
    value class UpdateAvatar(val byteArray: ByteArray) : UserInfoAction
    object UpdateUserInfo : UserInfoAction
}

@HiltViewModel
class UpdateUserInfoViewModel @Inject constructor(
    private val fileRepository: FileRepository,
    private val userRepository: UserRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserInfoUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()


    fun emitEvent(action: UserInfoAction) = when (action) {
        is UserInfoAction.UpdateNickname -> updateNickname(action.nickname)
        is UserInfoAction.UpdateAvatar -> updateAvatar(action.byteArray)
        is UserInfoAction.UpdateGender -> updateGender(action.gender)
        UserInfoAction.UpdateUserInfo -> updateUserInfo()
    }

    private fun updateNickname(v: String) {
        _uiState.update { it.copy(nickname = v) }
    }

    private fun updateGender(v: Gender) {
        _uiState.update { it.copy(gender = v) }
    }

    private fun updateUserInfo() {
        val (avatar, nickname, gender) = _uiState.updateAndGet { it.copy(loading = true) }
        if (nickname.isNotBlank() && gender != Gender.Unknown && avatar.isNotBlank()) {
            viewModelScope.launch(ioDispatcher) {
                userRepository.updateUserInfo(nickname, gender, avatar)
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    private fun updateAvatar(byteArray: ByteArray) {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch(ioDispatcher) {
            fileRepository.putPicture(byteArray).onSuccess { url ->
                _uiState.update { it.copy(avatar = url) }
            }.onFailure {
                _uiEvent.emit(UIEvent.ShowSnackBar(R.string.failed_upload_file))
            }
            _uiState.update { it.copy(loading = false) }
        }
    }
}