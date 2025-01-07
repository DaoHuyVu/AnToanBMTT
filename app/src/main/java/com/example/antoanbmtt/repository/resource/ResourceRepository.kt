package com.example.antoanbmtt.repository.resource

import com.example.antoanbmtt.api.ApiResult
import com.example.antoanbmtt.api.resource.DownloadResourceContent
import com.example.antoanbmtt.api.resource.ResourceContent
import com.example.antoanbmtt.api.resource.ResourceResponse
import com.example.antoanbmtt.api.resource.ResourceService
import com.example.antoanbmtt.api.resource.SharedResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.ResponseBody
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
    fun getSharedResources() : List<Resource>{
        return toSharedResources()
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
    suspend fun downloadContent(uri : String,fileName : String) : ApiResult<DownloadResourceContent>{
        return withContext(Dispatchers.IO){
            val response = resourceService.getResourceContent(uri)
            if(response.isSuccessful){
                ApiResult.Success(DownloadResourceContent(uri,fileName,response.body()))
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
                ApiResult.Failure("Can't restore resource from recycle bin")
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
    fun getResourceItem(id : Long) : ApiResult<ResourceDetails>{
        val temp = resources.find { it.id == id }
        if(temp != null){
            return ApiResult.Success(temp.toResourceDetails())
        }
        return ApiResult.Failure("Can't find resource item")
    }
    suspend fun updateSharedPassword(id : Long, password : String) : ApiResult<ResourceDetails>{
        return withContext(Dispatchers.IO){
            val response = resourceService.updateResourcePassword(password,id)
            if(response.isSuccessful){
                val index = resources.indexOfFirst { it.id == id }
                resources[index] = response.body()!!
                ApiResult.Success(resources[index].toResourceDetails())
            }
            else if(response.code() in 400..499)
                ApiResult.Failure("Can't update resource password")
            else ApiResult.Failure("Host error")
        }
    }
    suspend fun getSharedResource(uri : String) : ApiResult<SharedResource>{
        return withContext(Dispatchers.IO){
            val response = resourceService.getSharedResource(uri)
            if(response.isSuccessful){
                ApiResult.Success(response.body()!!)
            }
            else if(response.code() == 400)
                ApiResult.Failure("Wrong password")
            else if(response.code() == 403)
                ApiResult.Failure("Password is not provided")
            else ApiResult.Failure("Host error")
        }
    }
    suspend fun getSharedResourceContent(uri : String) : ApiResult<ResponseBody>{
        return withContext(Dispatchers.IO){
            val response = resourceService.getSharedResourceContent(uri)
            if(response.isSuccessful){
                ApiResult.Success(response.body()!!)
            }
            else if(response.code() in 400..499)
                ApiResult.Failure("Can't get received resources")
            else ApiResult.Failure("Host error")
        }
    }
    suspend fun getReceivedResources() : ApiResult<List<SharedResource>>{
        return withContext(Dispatchers.IO){
            val response = resourceService.getReceivedResources()
            if(response.isSuccessful){
                ApiResult.Success(response.body()!!)
            }
            else if(response.code() == 400)
                ApiResult.Failure("Wrong password")
            else if(response.code() == 403)
                ApiResult.Failure("Password is not provided")
            else ApiResult.Failure("Host error")
        }
    }
    suspend fun deleteLink(uri : String) : ApiResult<ResourceDetails>{
        return withContext(Dispatchers.IO){
            val response = resourceService.deleteLink(uri)
            if(response.isSuccessful){
                val index = resources.indexOfFirst { it.uri == uri }
                resources[index] = response.body()!!
                ApiResult.Success(resources[index].toResourceDetails())
            }
            else if(response.code() in 400..499)
                ApiResult.Failure("Can't delete link")
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
    private fun toSharedResources() : List<Resource>{
        return resources.filter { it.isShared }.map { resources -> resources.toResource() }
    }
}