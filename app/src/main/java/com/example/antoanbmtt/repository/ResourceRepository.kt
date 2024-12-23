package com.example.antoanbmtt.repository

import com.example.antoanbmtt.api.ApiResult
import com.example.antoanbmtt.api.resource.ResourceContent
import com.example.antoanbmtt.api.resource.ResourceResponse
import com.example.antoanbmtt.api.resource.ResourceService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceRepository @Inject constructor(
    private val resourceService: ResourceService
) {
    private var resources : MutableList<ResourceResponse> = mutableListOf()
    suspend fun getResources() : ApiResult<List<Resource>>{
        return withContext(Dispatchers.IO){
            val response = resourceService.getResources()
            if(response.isSuccessful){
                resources = response.body()!!.toMutableList()
                ApiResult.Success(resources.map { resource -> resource.toResource() })
            }
            else if(response.code() in 400..499)
                ApiResult.Failure("Can't fetch resources")
            else ApiResult.Failure("Host error")
        }
    }
    suspend fun postResource(file : MultipartBody.Part) : ApiResult<List<Resource>>{
        return withContext(Dispatchers.IO){
            val response = resourceService.postResource(file)
            if(response.isSuccessful){
                resources.add(response.body()!!)
                ApiResult.Success(resources.map { resource -> resource.toResource() })
            }
            else if(response.code() in 400..499)
                ApiResult.Failure("Can't upload resource")
            else ApiResult.Failure("Host error")
        }
    }
    suspend fun getResourceContent(uri : String) : ApiResult<ResourceContent>{
        return withContext(Dispatchers.IO){
            val response = resourceService.getResourceContent(uri)
            if(response.isSuccessful){
                ApiResult.Success(response.body()!!)
            }
            else if(response.code() in 400..499)
                ApiResult.Failure("Can't get resource content")
            else ApiResult.Failure("Host error")
        }
    }
}