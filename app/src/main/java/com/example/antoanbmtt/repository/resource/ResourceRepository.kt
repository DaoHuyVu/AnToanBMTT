package com.example.antoanbmtt.repository.resource

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
                ApiResult.Success(toUiResources())
            }
            else if(response.code() in 400..499)
                ApiResult.Failure("Can't fetch resources")
            else ApiResult.Failure("Host error")
        }
    }
    suspend fun getTempDeleteResources() : ApiResult<List<Resource>>{
        return withContext(Dispatchers.IO){
            val response = resourceService.getResources()
            if(response.isSuccessful){
                resources = response.body()!!.toMutableList()
                ApiResult.Success(toDeletedResources())
            }
            else if(response.code() in 400..499)
                ApiResult.Failure("Can't fetch resources")
            else ApiResult.Failure("Host error")
        }
    }
    suspend fun getFavouriteResources() : ApiResult<List<Resource>>{
        return withContext(Dispatchers.IO){
            val response = resourceService.getResources()
            if(response.isSuccessful){
                resources = response.body()!!.toMutableList()
                ApiResult.Success(toUiFavouriteResources())
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
                ApiResult.Success(toUiResources())
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
                ApiResult.Success(ResourceContent(uri,response.body()!!))
            }
            else if(response.code() in 400..499)
                ApiResult.Failure("Can't get resource content")
            else ApiResult.Failure("Host error")
        }
    }
    suspend fun tempDeleteResource(fields : String, id : Long) : ApiResult<List<Resource>>{
        return withContext(Dispatchers.IO){
            val response = resourceService.updateResourceInfo(fields,id)
            if(response.isSuccessful){
                val index = resources.indexOfFirst { resource -> resource.id == id }
                if(index != -1){
                    resources[index] = response.body()!!
                }
                else{
                    ApiResult.Failure("Can't find item within resources")
                }
                ApiResult.Success(toUiResources())
            }
            else if(response.code() in 400..499)
                ApiResult.Failure("Can't temp delete info")
            else ApiResult.Failure("Host error")
        }
    }
    suspend fun restoreResourceFromTempDelete(fields : String, id : Long) : ApiResult<List<Resource>>{
        return withContext(Dispatchers.IO){
            val response = resourceService.updateResourceInfo(fields,id)
            if(response.isSuccessful){
                val index = resources.indexOfFirst { resource -> resource.id == id }
                if(index != -1){
                    resources[index] = response.body()!!
                }
                else{
                    ApiResult.Failure("Can't find item within resources")
                }
                ApiResult.Success(toDeletedResources())
            }
            else if(response.code() in 400..499)
                ApiResult.Failure("Can't temp delete info")
            else ApiResult.Failure("Host error")
        }
    }
    suspend fun updateFavourite(fields : String, id : Long) : ApiResult<List<Resource>>{
        return withContext(Dispatchers.IO){
            val response = resourceService.updateResourceInfo(fields,id)
            if(response.isSuccessful){
                val index = resources.indexOfFirst { resource -> resource.id == id }
                if(index != -1){
                    resources[index] = response.body()!!
                }
                else{
                    ApiResult.Failure("Can't find item within resources")
                }
                ApiResult.Success(toUiResources())
            }
            else if(response.code() in 400..499)
                ApiResult.Failure("Can't update favourite info")
            else ApiResult.Failure("Host error")
        }
    }
    // Return resources displayed in UI ( a.k.a not temp deleted )
    private fun toUiResources() : List<Resource>{
        return resources.filter { !it.isTempDelete }.map{ resources -> resources.toResource()}
    }
    private fun toUiFavouriteResources() : List<Resource>{
            return resources.filter { !it.isTempDelete }.filter { it.isFavourite }.map{ resources -> resources.toResource()}
    }
    private fun toDeletedResources() : List<Resource> {
        return resources.filter { it.isTempDelete }.map { resources -> resources.toResource() }
    }
}