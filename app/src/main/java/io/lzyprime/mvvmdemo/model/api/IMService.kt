package io.lzyprime.mvvmdemo.model.api

import com.tencent.imsdk.v2.V2TIMGroupInfo
import com.tencent.imsdk.v2.V2TIMManager
import com.tencent.imsdk.v2.V2TIMValueCallback
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

interface IMService {
    /** 获取已加入群聊列表. */
    suspend fun getJoinedGroupList(): List<V2TIMGroupInfo>
}

class IMServiceImpl : IMService {
    override suspend fun getJoinedGroupList(): List<V2TIMGroupInfo> =
        suspendCancellableCoroutine { continuation ->
            MutableStateFlow()
            V2TIMManager.getGroupManager().getJoinedGroupList(object : V2TIMValueCallback<List<V2TIMGroupInfo>> {
                    override fun onSuccess(t: List<V2TIMGroupInfo>) {
                        continuation.resume(t)
                    }

                    override fun onError(code: Int, desc: String?) {
                        continuation.resume(emptyList())
//                     continuation.resumeWithException(Exception("code: $code, desc: $desc"))
                    }
                })

//            continuation.invokeOnCancellation {
//
//            }
        }
}