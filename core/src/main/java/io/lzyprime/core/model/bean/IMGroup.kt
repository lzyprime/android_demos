package io.lzyprime.core.model.bean

import com.tencent.imsdk.v2.V2TIMGroupAtInfo
import com.tencent.imsdk.v2.V2TIMManager

enum class GroupType(val value: String) {
    None("undefined type"),
    Work(V2TIMManager.GROUP_TYPE_WORK),
    Public(V2TIMManager.GROUP_TYPE_PUBLIC),
    Meeting(V2TIMManager.GROUP_TYPE_MEETING),
    AVChatRoom(V2TIMManager.GROUP_TYPE_AVCHATROOM);

    companion object {
        operator fun invoke(value: String): GroupType = values().find { it.value == value } ?: None
    }
}

sealed class GroupAtMsg(val seq: Long) {
    class None(seq: Long) : GroupAtMsg(seq)
    class AtAll(seq: Long) : GroupAtMsg(seq)
    class AtMe(seq: Long) : GroupAtMsg(seq)
    class AtAllAndAtMe(seq: Long) : GroupAtMsg(seq)

    companion object {
        const val AT_ALL_TAG = V2TIMGroupAtInfo.AT_ALL_TAG
        operator fun invoke(v2TIMGroupAtInfo: V2TIMGroupAtInfo): GroupAtMsg =
            when (v2TIMGroupAtInfo.atType) {
                V2TIMGroupAtInfo.TIM_AT_ALL -> AtAll(v2TIMGroupAtInfo.seq)
                V2TIMGroupAtInfo.TIM_AT_ME -> AtMe(v2TIMGroupAtInfo.seq)
                V2TIMGroupAtInfo.TIM_AT_ALL_AT_ME -> AtAllAndAtMe(v2TIMGroupAtInfo.seq)
                else -> None(v2TIMGroupAtInfo.seq)
            }
    }
}