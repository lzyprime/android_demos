package io.lzyprime.mvvmdemo.model.bean

import com.tencent.imsdk.v2.V2TIMManager.*

sealed class IMLoginStatus(val value: Int) {

    object LoggingIn : IMLoginStatus(V2TIM_STATUS_LOGINING)
    object Login : IMLoginStatus(V2TIM_STATUS_LOGINED)

    abstract class Logout : IMLoginStatus(V2TIM_STATUS_LOGOUT)
    object NoLogin : Logout()
    object Kicked : Logout()
    object SigExpired : Logout()
}