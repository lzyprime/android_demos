package io.lzyprime.mvvmdemo.model.api

import com.tencent.imsdk.v2.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/** IM请求. */
interface IMService {
    /** 获取已加入群聊列表. */
    suspend fun getJoinedGroupList(): List<V2TIMGroupInfo>

    /** 获取全部会话列表. */
    suspend fun getConversionList(): List<V2TIMConversation>

    /** 获取指定id会话列表. */
    suspend fun getConversionList(conversionIds: List<String>): List<V2TIMConversation>

    /** 批量获取群信息. */
    suspend fun getMultiGroupInfo(groupIds: List<String>): List<V2TIMGroupInfoResult>

    /** 获取群成员列表. */
    suspend fun getGroupMemberList(groupId: String): List<V2TIMGroupMemberFullInfo>

    /** 批量获取群成员信息. */
    suspend fun getMultiGroupMemberInfo(
        groupId: String,
        userIds: List<String>
    ): List<V2TIMGroupMemberFullInfo>

    /** 修改群成员信息. */
    suspend fun setGroupMemberInfo(groupId: String, member: V2TIMGroupMemberFullInfo): Boolean

    /** 解散群聊. */
    suspend fun dismissGroup(groupId: String): Boolean

    /** 删除会话及聊天记录. */
    suspend fun deleteConversion(groupId: String): Boolean

    /** 设置群信息. */
    suspend fun setGroupInfo(info: V2TIMGroupInfo): Boolean

    /** 删除群成员. */
    suspend fun kickGroupMember(groupId: String, userIds: List<String>): Boolean

    /** 修改群成员身份. */
    suspend fun setGroupMemberRole(groupId: String, userId: String, newRole: Int): Boolean

    /** 群消息免打扰. */
    suspend fun setGroupReceiveMessageOpt(groupId: String, receiveType: Int): Boolean

    /** 标记消息已读. */
    suspend fun markGroupMessageAsRead(groupId: String): Boolean

    /** 获取群历史消息. */
    suspend fun getGroupHistoryMessageList(
        groupId: String,
        v2TIMMessage: V2TIMMessage?
    ): List<V2TIMMessage>

    /** 发送消息. */
    suspend fun sendMessage(groupId: String, v2TIMMessage: V2TIMMessage): V2TIMMessage?

    /** 登录IM. */
    suspend fun login(userId: String, sig: String): Boolean

    /** 退出登录. */
    suspend fun logout(): Boolean

    /** 批量查找用户. */
    suspend fun getMultiUserInfoById(userIds: List<String>): List<V2TIMUserFullInfo>

    /** 设置个人信息. */
    suspend fun setSelfUserInfo(v2TIMUserFullInfo: V2TIMUserFullInfo): Boolean
}