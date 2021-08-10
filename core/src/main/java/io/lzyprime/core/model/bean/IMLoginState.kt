package io.lzyprime.core.model.bean

import com.tencent.imsdk.v2.V2TIMManager.*

sealed class IMLoginState(val value: Int) {

    object LoggingIn : IMLoginState(V2TIM_STATUS_LOGINING)
    class Login(val userId: String) : IMLoginState(V2TIM_STATUS_LOGINED)

    abstract class Logout : IMLoginState(V2TIM_STATUS_LOGOUT)
    object NoLogin : Logout()
    object Kicked : Logout()
    object SigExpired : Logout()
}