package com.example.antoanbmtt.interceptor

import com.example.antoanbmtt.repository.UserDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Singleton

@Singleton
class TokenInterceptor(
    private val dataStore : UserDataStore
) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = runBlocking { dataStore.tokenFlow.first()}
        val newRequest = request.newBuilder().addHeader("Authorization","Bearer $token").build()
        return chain.proceed(newRequest)
    }
}