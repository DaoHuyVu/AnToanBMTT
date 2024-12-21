package com.example.antoanbmtt.api.login

data class LoginResponse(
    val accessToken : String,
    val userName : String,
    val email : String
)
