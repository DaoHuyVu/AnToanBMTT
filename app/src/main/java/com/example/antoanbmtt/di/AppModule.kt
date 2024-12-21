package com.example.antoanbmtt.di

import com.example.antoanbmtt.annotation.TokenInterceptorOkHttp
import com.example.antoanbmtt.api.login.AccountService
import com.example.antoanbmtt.interceptor.TokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofitBuilder() : Builder{
        return Builder().baseUrl("https://terrier-modern-violently.ngrok-free.app")
            .addConverterFactory(GsonConverterFactory.create())
    }
    @Provides
    @Singleton
    @TokenInterceptorOkHttp
    fun provideInterceptorOkHttp(
        tokenInterceptor: TokenInterceptor
    ) : OkHttpClient = OkHttpClient.Builder().addInterceptor(tokenInterceptor).build()
    @Provides
    @Singleton
    fun provideAccountService(
        builder : Builder
    ) : AccountService{
        return builder.build().create(AccountService::class.java)
    }
}