package com.example.antoanbmtt.api.resource

import okhttp3.ResponseBody

data class ResourceContent(
    val uri : String? = null,
    val content : ResponseBody? = null
)
