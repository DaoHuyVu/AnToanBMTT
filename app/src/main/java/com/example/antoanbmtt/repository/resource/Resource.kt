package com.example.antoanbmtt.repository.resource

data class Resource(
    val id : Long,
    val name : String,
    val lastUpdate : String,
    val capacity : String,
    val uri : String,
    val isFavourite: Boolean,
    val isTempDelete : Boolean
)
