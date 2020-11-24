package io.lzyprime.mvvmdemo.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lzyprime.mvvmdemo.model.UserRepository
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
):ViewModel() {
    val loginStatus get() = userRepository.loginStatus
    suspend fun login(userId:String, sig:String) = userRepository.login(userId, sig)
}
