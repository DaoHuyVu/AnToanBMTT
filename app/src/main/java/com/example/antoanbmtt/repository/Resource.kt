package com.example.antoanbmtt.repository

import java.time.LocalDateTime

data class Resource(
    val name : String,
    val lastUpdate : String,
    val capacity : String,
    val uri : String,
    val isFavourite: Boolean,
    val isTempDelete : Boolean
)
