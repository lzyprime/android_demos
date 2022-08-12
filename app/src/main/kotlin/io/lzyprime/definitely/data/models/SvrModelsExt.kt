package io.lzyprime.definitely.data.models

import io.lzyprime.svr.model.Gender
import io.lzyprime.svr.model.UserInfo

val UserInfo.needComplete: Boolean
    inline get() =
        avatar.isBlank() || nickname.isBlank() || gender == Gender.Unknown