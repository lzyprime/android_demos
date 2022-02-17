package io.lzyprime.definitely.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lzyprime.core.data.models.unsplash.UnsplashUser
import io.lzyprime.core.domain.LoginUnsplashUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val loginUnsplashUseCase: LoginUnsplashUseCase,
) : ViewModel() {
    private val _hadLogin = MutableStateFlow<Boolean?>(null)
    val hadLogin = _hadLogin.asStateFlow()

    private val _unsplashUser = MutableStateFlow<UnsplashUser?>(null)
    val unsplashUser = _unsplashUser.asStateFlow()

    init {
        viewModelScope.launch {
            _hadLogin.value = loginUnsplashUseCase()
        }
    }

    fun login(unsplashUserCode: String) {
        if (unsplashUserCode.isNotEmpty() ||
            _hadLogin.compareAndSet(false, null)
        ) {
            viewModelScope.launch {
                _hadLogin.value = loginUnsplashUseCase(unsplashUserCode)
            }
        }
    }
}