package io.lzyprime.definitely.viewmodel

import io.lzyprime.definitely.R
import io.lzyprime.svr.model.Failed

val Throwable?.stringResId:Int? get() = when(this) {
    is Failed.TokenExpired -> R.string.msg_token_expired
    else -> null
}