package com.example.antoanbmtt.api.login

import com.example.antoanbmtt.api.ApiMessage
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    @POST("/account/login")
    suspend fun login(@Body loginRequest: LoginRequest) : Response<LoginResponse>
    @POST("/account/signUp")
    suspend fun signUp(@Body signUpRequest: SignUpRequest) : Response<ApiMessage>
    @POST("/account/login/biometrics")
    @FormUrlEncoded
    suspend fun loginBiometrics(
        @Field("email") email : String,
        @Field("name") name : String
    ) : Response<LoginResponse>
    @POST("/account/publicKey")
    @FormUrlEncoded
    suspend fun exchangePublicKey(
        @Field("publicKey") key : String,
        @Field("email") email : String
    ) : Response<PublicKey>
}