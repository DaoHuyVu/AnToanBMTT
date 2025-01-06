package com.example.antoanbmtt.di

import com.example.antoanbmtt.SERVER_HOST
import com.example.antoanbmtt.annotation.TimeoutAnnotationOkHttp
import com.example.antoanbmtt.annotation.TokenInterceptorOkHttp
import com.example.antoanbmtt.api.account.AccountService
import com.example.antoanbmtt.api.login.AuthService
import com.example.antoanbmtt.api.resource.ResourceService
import com.example.antoanbmtt.api.user.UserService
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
        return Builder().baseUrl(SERVER_HOST)
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
    fun provideAuthService(
        builder : Builder
    ) : AuthService{
        return builder.build().create(AuthService::class.java)
    }
    @Provides
    @Singleton
    fun provideResourceService(
        builder: Builder,
        @TokenInterceptorOkHttp okHttpClient: OkHttpClient
    ) : ResourceService {
        return builder.client(okHttpClient).build().create(ResourceService::class.java)
    }
    @Provides
    @Singleton
    fun provideUserService(
        builder: Builder,
        @TokenInterceptorOkHttp okHttpClient: OkHttpClient
    ) : UserService {
        return builder.client(okHttpClient).build().create(UserService::class.java)
    }
    @Provides
    @Singleton
    fun provideAccountService(
        builder: Builder,
        @TokenInterceptorOkHttp okHttpClient: OkHttpClient
    ) :  AccountService{
        return builder.client(okHttpClient).build().create(AccountService::class.java)
    }
}