package io.lzyprime.mvvmdemo.extension

import com.tencent.imsdk.v2.V2TIMUserFullInfo
import io.lzyprime.mvvmdemo.model.bean.IMUser

fun V2TIMUserFullInfo.toIMUser() = IMUser(userID.orEmpty())