package com.example.antoanbmtt.repository.resource

data class ResourceDetails(
    val id : Long,
    val name : String,
    val uploadTime : String,
    val lastUpdate : String,
    val capacity : String,
    val uri : String,
    val isFavourite : Boolean,
    val isTempDelete : Boolean
)
