package com.example.antoanbmtt.api.resource

import okhttp3.ResponseBody

data class DownloadResourceContent(
    val uri : String? = null,
    val fileName : String ? = null,
    val downloadContent : ResponseBody? = null
)
