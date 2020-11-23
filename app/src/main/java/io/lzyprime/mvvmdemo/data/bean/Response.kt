package io.lzyprime.mvvmdemo.data.bean

sealed class Response<out T> {
    data class Success<T>(val data: T) : Response<T>()
    object Failed : Response<Nothing>()
}