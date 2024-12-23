package com.example.antoanbmtt.api.login

data class LoginResponse(
    val accessToken : String,
    val userInfo: UserInfo
)
data class UserInfo(
    val userName : String,
    val email : String
)
