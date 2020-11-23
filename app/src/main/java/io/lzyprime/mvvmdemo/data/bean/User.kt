package io.lzyprime.mvvmdemo.data.bean

data class User(
    val id:String,
    val updated_at:String = "",
    val username:String="",
    val name:String = "",
    val first_name:String = "",
    val last_name:String = "",
)