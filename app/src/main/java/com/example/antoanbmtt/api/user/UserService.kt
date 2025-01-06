package com.example.antoanbmtt.api.user

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.PATCH

interface UserService {
    @PATCH("/user")
    @FormUrlEncoded
    suspend fun updateUserName(
        @Field("userName") userName : String,
        @Field("email") email : String
    ) : Response<Unit>
}