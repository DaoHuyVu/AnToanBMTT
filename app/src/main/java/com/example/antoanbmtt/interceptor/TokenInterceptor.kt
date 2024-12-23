package com.example.antoanbmtt.interceptor

import com.example.antoanbmtt.repository.UserDataStore
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor(
    private val dataStore : UserDataStore
) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = dataStore.getToken()
        val newRequest = request.newBuilder().addHeader("Authorization","Bearer $token").build()
        return chain.proceed(newRequest)
    }
}