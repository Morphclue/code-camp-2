package org.feature.fox.coffee_counter.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.feature.fox.coffee_counter.BaseApplication

import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(BaseApplication::class)
object ClientModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(
        logingInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

}