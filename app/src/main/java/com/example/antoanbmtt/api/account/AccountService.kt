package com.example.antoanbmtt.api.account

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.PATCH

interface AccountService {
    @PATCH("/account/password")
    @FormUrlEncoded
    suspend fun updatePassword(
        @Field("oldPassword") oldPassword : String,
        @Field("newPassword") newPassword : String,
        @Field("email") email : String
    ) : Response<Unit>
    @PATCH("/account/email")
    @FormUrlEncoded
    suspend fun updateEmail(
        @Field("newEmail") newEmail : String,
        @Field("email") email : String
    ) : Response<Unit>

}