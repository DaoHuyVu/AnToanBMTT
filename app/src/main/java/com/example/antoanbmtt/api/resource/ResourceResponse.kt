package com.example.antoanbmtt.api.resource

import com.example.antoanbmtt.helper.formatDate
import com.example.antoanbmtt.helper.toByteRepresentation
import com.example.antoanbmtt.repository.resource.Resource

data class ResourceResponse(
    val id : Long,
    val name : String,
    val uploadTime : String,
    val lastUpdate : String,
    val capacity : Long,
    val uri : String,
    val isFavourite : Boolean,
    val isTempDelete : Boolean
){
    fun toResource() : Resource {
        return Resource(id,name,lastUpdate.formatDate(),capacity.toByteRepresentation(),uri,isFavourite,isTempDelete)
    }
}
