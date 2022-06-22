package org.feature.fox.coffee_counter.di.services.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Singleton

@Singleton
class BearerInterceptor : Interceptor {

    var bearerToken: String = ""
    var expiration: Long = 0

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $bearerToken").build()

        return chain.proceed(request)
    }
}
