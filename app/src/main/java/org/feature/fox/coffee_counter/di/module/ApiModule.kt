package org.feature.fox.coffee_counter.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.di.services.network.ApiService
import org.feature.fox.coffee_counter.di.services.network.BearerInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun providesBaseUrl(): String {
        return BuildConfig.BASE_URL
    }

    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun providesBearerInterceptor(): BearerInterceptor {
        return BearerInterceptor()
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        bearerInterceptor: BearerInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(bearerInterceptor)
            .callTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(
        baseUrl: String,
        convFact: Converter.Factory,
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(convFact)
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun providesConverterFactory(): Converter.Factory {
        return MoshiConverterFactory.create().asLenient()
    }

    @Singleton
    @Provides
    fun providesApiService(
        retrofit: Retrofit = providesRetrofit(
            BuildConfig.BASE_URL,
            providesConverterFactory(),
            providesOkHttpClient(
                providesLoggingInterceptor(),
                providesBearerInterceptor()
            )
        ),
    ): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
