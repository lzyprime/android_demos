package io.lzyprime.svr.model

sealed interface UserInfoState
data class UserInfo internal constructor(
    val id: Int,
    val name: String,
    val nickname: String,
    val gender: Gender,
    val avatar: String,
):UserInfoState {
    companion object None:UserInfoState
}

@kotlinx.serialization.Serializable
internal data class UserInfoSvrModel(
    val uid: Int,
    val name: String,
    val nickname: String,
    val gender: Int,
    val avatar: String,
) {
    fun toUserInfo(): UserInfo = UserInfo(
        uid,
        name,
        nickname,
        Gender(gender),
        avatar,
    )
}