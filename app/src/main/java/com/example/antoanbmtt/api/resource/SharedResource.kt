package com.example.antoanbmtt.api.resource

import okhttp3.ResponseBody

data class SharedResource(
    val name : String,
    val capacity : Long,
    val content : ResponseBody? = null,
)

