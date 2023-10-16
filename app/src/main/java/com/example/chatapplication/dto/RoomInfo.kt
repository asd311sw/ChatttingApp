package com.example.chatapplication.dto

data class RoomInfo(
    val otherUserId:String,
    val otherUserInfo: UserInfo,
    val lastContent:String,
    val lastWrittenTime:String
)
