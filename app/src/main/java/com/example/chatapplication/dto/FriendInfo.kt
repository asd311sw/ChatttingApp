package com.example.chatapplication.dto

import android.net.Uri

data class FriendInfo(
    var userId:String,
    var userName:String,
    var introduce:String,
    var state:String,
    var birthDate:String,
    var personality:String,
    var sex:String,
    var imageUri: String? = null,
    var storyCount:Int = 0,
    var followingCount:Int = 0,
    var followerCount:Int = 0
)
