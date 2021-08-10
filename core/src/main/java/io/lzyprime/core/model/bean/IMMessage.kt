package io.lzyprime.core.model.bean

import com.tencent.imsdk.v2.V2TIMMessage

class IMMessage(private val v2TIMMessage: V2TIMMessage) {
    val msgId:String by lazy { v2TIMMessage.msgID.orEmpty() }

}