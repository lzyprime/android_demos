package io.lzyprime.svr.model

sealed class Failed : Throwable() {
    object Unknown : Failed()
    object UserOrPasswordError : Failed()
    object TokenExpired : Failed()
    object UserNotExist : Failed()

    sealed class LocalFailed : Failed()
    object ParseBodyFailed : LocalFailed()
    object TokenEmpty : LocalFailed()
    object AlreadyLogin : LocalFailed()

    companion object {
        internal operator fun invoke(code: Int): Throwable =
            when (code) {
                10000 -> UserOrPasswordError
                10001 -> TokenExpired
                10002 -> UserNotExist
                else -> Unknown
            }
    }
}

fun Throwable.toFailed(): Failed = (this as? Failed) ?: Failed.Unknown