package io.lzyprime.core.model.bean

import com.tencent.imsdk.v2.V2TIMConversation

data class IMConversation(private val v2TIMConversation: V2TIMConversation) {
    val conversationId: String by lazy { v2TIMConversation.conversationID.orEmpty() }
    val title: String by lazy { v2TIMConversation.showName.orEmpty() }
    val avatar: String by lazy { v2TIMConversation.faceUrl.orEmpty() }
    val type: IMConversationType by lazy { IMConversationType(v2TIMConversation) }
    val lastMessage: IMMessage? by lazy { v2TIMConversation.lastMessage?.let { IMMessage(it) } }
    val unreadCount: Int by lazy { v2TIMConversation.unreadCount }
    val isPinned: Boolean by lazy { v2TIMConversation.isPinned }
    val orderKey: Long by lazy { v2TIMConversation.orderKey }
    val draftText: String by lazy { v2TIMConversation.draftText }
    val draftTimestamp: Timestamp by lazy { Timestamp(v2TIMConversation.draftTimestamp) }
}

sealed class IMConversationType(val value: Int) {
    object None : IMConversationType(V2TIMConversation.CONVERSATION_TYPE_INVALID)

    data class C2C(val userId: String) : IMConversationType(V2TIMConversation.V2TIM_C2C)

    data class Group(
        val groupId: String,
        val groupType: GroupType,
        val atMsgList: List<GroupAtMsg>,
    ) : IMConversationType(V2TIMConversation.V2TIM_GROUP)

    companion object {
        operator fun invoke(v2TIMConversation: V2TIMConversation): IMConversationType =
            when (v2TIMConversation.type) {
                V2TIMConversation.V2TIM_C2C -> C2C(v2TIMConversation.userID)
                V2TIMConversation.V2TIM_GROUP -> Group(
                    v2TIMConversation.groupID,
                    GroupType(v2TIMConversation.groupType),
                    v2TIMConversation.groupAtInfoList.orEmpty().map { GroupAtMsg(it) },
                )
                else -> None
            }
    }
}


