package io.lzyprime.mvvmdemo.model

import android.app.Application
import com.tencent.imsdk.v2.V2TIMManager
import com.tencent.imsdk.v2.V2TIMSDKConfig
import io.lzyprime.mvvmdemo.extension.V2TIMCallback
import io.lzyprime.mvvmdemo.extension.V2TIMSDKListener
import io.lzyprime.mvvmdemo.model.bean.IMLoginStatus
import io.lzyprime.mvvmdemo.utils.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class UserRepository @Inject constructor(
    application: Application,
    private val manager: V2TIMManager,
) {
    private val _loginStatus = MutableStateFlow<IMLoginStatus>(IMLoginStatus.NoLogin)
    val loginStatus = _loginStatus.asStateFlow()

    init {
        val config = V2TIMSDKConfig()
        config.logLevel = V2TIMSDKConfig.V2TIM_LOG_DEBUG

        manager.initSDK(
            application.applicationContext, IM_SDK_APP_ID, config, V2TIMSDKListener(
                onKickedOffline = {
                    _loginStatus.value = IMLoginStatus.Kicked
                },
                onUserSigExpired = {
                    _loginStatus.value = IMLoginStatus.SigExpired
                }
            )
        )
    }

    /** 登录IM. */
    suspend fun login(userId: String, sig: String): Boolean =
        suspendCancellableCoroutine { continuation ->
            _loginStatus.value = IMLoginStatus.LoggingIn
            manager.login(userId, sig, V2TIMCallback({
                _loginStatus.value = IMLoginStatus.Login
                continuation.resume(true)
            }, {
                _loginStatus.value = IMLoginStatus.NoLogin
                continuation.resume(false)
            }))
        }
}