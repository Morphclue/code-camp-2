package org.feature.fox.coffee_counter.di.services.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.di.services.AppPreference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BearerInterceptor @Inject constructor(
    private val preference: AppPreference,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val bearerToken = preference.getTag(BuildConfig.BEARER_TOKEN)
        val request: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $bearerToken")
            .build()

        return chain.proceed(request)
    }
}
