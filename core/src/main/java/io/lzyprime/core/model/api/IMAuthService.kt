package io.lzyprime.core.model.api

import android.app.Application
import com.tencent.imsdk.v2.V2TIMManager
import com.tencent.imsdk.v2.V2TIMSDKConfig
import io.lzyprime.core.model.bean.IMLoginState
import io.lzyprime.core.utils.IM_SDK_APP_ID
import io.lzyprime.core.utils.V2TIMCallback
import io.lzyprime.core.utils.V2TIMSDKListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/** IM请求. */
interface IMAuthService {
    // 登录状态.
    val loginState: StateFlow<IMLoginState>

    /** 登录IM. */
    suspend fun login(userId: String, sig: String): Boolean

    /** 退出登录. */
    suspend fun logout(): Boolean

    companion object {
        operator fun invoke(
            application: Application,
            manager: V2TIMManager,
        ):IMAuthService = object : IMAuthService {
            private val mutableLoginState = MutableStateFlow<IMLoginState>(IMLoginState.NoLogin)
            override val loginState = mutableLoginState.asStateFlow()

            init {
                val config = V2TIMSDKConfig()
                config.logLevel = V2TIMSDKConfig.V2TIM_LOG_DEBUG
                manager.initSDK(
                    application, IM_SDK_APP_ID, config, V2TIMSDKListener(
                        onKickedOffline = {
                            mutableLoginState.value = IMLoginState.Kicked
                        },
                        onUserSigExpired = {
                            mutableLoginState.value = IMLoginState.SigExpired
                        },
                    )
                )
            }

            override suspend fun login(userId: String, sig: String): Boolean =
                suspendCancellableCoroutine { ctn ->
                    mutableLoginState.value = IMLoginState.LoggingIn
                    manager.login(userId, sig, V2TIMCallback({
                        mutableLoginState.value = IMLoginState.Login(userId)
                        ctn.resume(true)
                    }, {
                        mutableLoginState.value = IMLoginState.NoLogin
                        ctn.resume(false)
                    }))
                }

            override suspend fun logout(): Boolean =
                suspendCancellableCoroutine { ctn ->
                    manager.logout(V2TIMCallback({
                        mutableLoginState.value = IMLoginState.NoLogin
                        ctn.resume(true)
                    }, {
                        ctn.resume(false)
                    }))
                }
        }
    }
}