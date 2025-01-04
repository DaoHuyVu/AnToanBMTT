package com.example.antoanbmtt.api.resource

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ResourceService {
    @GET("resource")
    suspend fun getResources() : Response<Collection<ResourceResponse>>
    @POST("resource")
    @Multipart
    suspend fun postResource(
        @Part file : MultipartBody.Part
    ) : Response<ResourceResponse>
    @GET("resource/{uri}")
    suspend fun getResourceContent(
        @Path("uri") uri : String
    ) : Response<ResponseBody>
    @PATCH("resource")
    @FormUrlEncoded
    suspend fun updateResourceInfo(
        @Field("fields") fields : String,
        @Field("id") id : Long
    ) : Response<ResourceResponse>
}