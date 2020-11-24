package io.lzyprime.mvvmdemo.model.bean

sealed class Res<out T> {
    data class Success<T>(val data: T) : Res<T>()
    data class Failed(val exception: Exception) : Res<Nothing>()
}

class CodeException(val code: Int, message: String) : Exception(message)