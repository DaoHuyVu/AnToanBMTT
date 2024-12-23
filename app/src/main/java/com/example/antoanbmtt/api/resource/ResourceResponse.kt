package com.example.antoanbmtt.api.resource

import android.annotation.SuppressLint
import com.example.antoanbmtt.helper.formatDate
import com.example.antoanbmtt.helper.toByteRepresentation
import com.example.antoanbmtt.repository.Resource
import java.time.LocalDateTime

data class ResourceResponse(
    val name : String,
    val uploadTime : String,
    val lastUpdate : String,
    val capacity : Long,
    val uri : String,
    val isFavourite : Boolean,
    val isTempDelete : Boolean
){
    @SuppressLint("NewApi")
    fun toResource() : Resource {
        return Resource(name,uploadTime.formatDate(),capacity.toByteRepresentation(),uri,isFavourite,isTempDelete)
    }
}
