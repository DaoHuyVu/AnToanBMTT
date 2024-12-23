package com.example.antoanbmtt.di

import com.example.antoanbmtt.annotation.TimeoutAnnotationOkHttp
import com.example.antoanbmtt.annotation.TokenInterceptorOkHttp
import com.example.antoanbmtt.api.login.AccountService
import com.example.antoanbmtt.api.resource.ResourceService
import com.example.antoanbmtt.interceptor.TokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    @TimeoutAnnotationOkHttp
    fun provideTimeoutInterceptor(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30,TimeUnit.SECONDS)
        .readTimeout(1,TimeUnit.MINUTES)
        .writeTimeout(1,TimeUnit.MINUTES)
        .build()

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        @TimeoutAnnotationOkHttp okHttpClient: OkHttpClient
    ) : Builder{
        return Builder().baseUrl("https://terrier-modern-violently.ngrok-free.app")
            .client(okHttpClient)
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
    @Provides
    @Singleton
    fun provideResourceService(
        builder: Builder,
        @TokenInterceptorOkHttp okHttpClient: OkHttpClient
    ) : ResourceService {
        return builder.client(okHttpClient).build().create(ResourceService::class.java)
    }
}