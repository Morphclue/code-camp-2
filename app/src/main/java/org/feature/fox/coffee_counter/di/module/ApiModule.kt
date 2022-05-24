package org.feature.fox.coffee_counter.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.feature.fox.coffee_counter.BaseApplication
import org.feature.fox.coffee_counter.data.network.apiservice.ApiService
import org.feature.fox.coffee_counter.util.Constants
import org.feature.fox.coffee_counter.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(BaseApplication::class)
object ApiModule {


    @Singleton
    @Provides
    fun provideApi(): ApiService {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiService::class.java)
    }

}