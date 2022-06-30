package io.lzyprime.svr.model

sealed interface Failed {
    object Unknown : Failed, Throwable()
    object UserOrPasswordError : Failed, Throwable()
    object TokenExpired : Failed, Throwable()
    object UserNotExist : Failed, Throwable()
    object ParseBodyFailed : Failed, Throwable()
    object NotLogin: Failed, Throwable()

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