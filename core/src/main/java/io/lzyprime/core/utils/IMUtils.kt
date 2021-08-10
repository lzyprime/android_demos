package io.lzyprime.core.utils

import com.tencent.imsdk.v2.*
import io.lzyprime.core.model.bean.CodeException

/** V2TIMSDKListener Builder.
 * @param onConnecting SDK 正在连接到腾讯云服务器
 * @param onConnectSuccess SDK 已经成功连接到腾讯云服务器
 * @param onConnectFailed SDK 连接腾讯云服务器失败
 * @param onKickedOffline 当前用户被踢下线
 * @param onUserSigExpired 在线时票据过期
 * @param onSelfInfoUpdated 登录用户的资料发生了更新
 * */
fun V2TIMSDKListener(
    onConnecting: () -> Unit = {},
    onConnectSuccess: () -> Unit = {},
    onConnectFailed: (CodeException) -> Unit = {},
    onKickedOffline: () -> Unit = {},
    onUserSigExpired: () -> Unit = {},
    onSelfInfoUpdated: (info: V2TIMUserFullInfo) -> Unit = {},
) = object : V2TIMSDKListener() {
    override fun onConnecting() = onConnecting()

    override fun onConnectSuccess() = onConnectSuccess()

    override fun onConnectFailed(code: Int, error: String) =
        onConnectFailed(CodeException(code, error))

    override fun onKickedOffline() = onKickedOffline()

    override fun onUserSigExpired() = onUserSigExpired()

    override fun onSelfInfoUpdated(info: V2TIMUserFullInfo) = onSelfInfoUpdated(info)
}

fun V2TIMCallback(
    onSuccess: () -> Unit = {},
    onFailed: (CodeException) -> Unit = {},
) = object : V2TIMCallback {
    override fun onSuccess() = onSuccess()
    override fun onError(code: Int, desc: String) = onFailed(CodeException(code, desc))
}

inline fun <reified T> V2TIMValueCallback(
    crossinline onSuccess: (T) -> Unit,
    crossinline onFailed: (CodeException) -> Unit,
) = object : V2TIMValueCallback<T> {
    override fun onSuccess(t: T) = onSuccess(t)

    override fun onError(code: Int, desc: String?) = onFailed(CodeException(code, desc.orEmpty()))
}
