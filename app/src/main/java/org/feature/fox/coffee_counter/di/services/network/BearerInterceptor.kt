package org.feature.fox.coffee_counter.di.services.network

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.data.models.body.LoginBody
import org.feature.fox.coffee_counter.di.services.AppPreference
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * Interceptor for adding authentication headers to requests.
 *
 * @property preference the AppPreference service.
 * @property apiService the ApiService.
 */
@Singleton
class BearerInterceptor @Inject constructor(
    private val preference: AppPreference,
    private val apiService: Provider<ApiService>,
) : Interceptor {
    /**
     * Intercepts a request and adds authentication headers to it.
     * @param chain the chain of interceptors.
     * @return the response.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(buildRequest(chain))
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            synchronized(this) {
                runBlocking {
                    refreshBearer()
                    response.close()
                    response = chain.proceed(buildRequest(chain))
                }
            }
        }
        return response
    }

    /**
     * Builds a request with authentication headers.
     * @param chain the chain of interceptors.
     * @return the request.
     */
    private fun buildRequest(chain: Interceptor.Chain): Request {
        val bearerToken = preference.getTag(BuildConfig.BEARER_TOKEN)
        return chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $bearerToken")
            .build()
    }

    /**
     * Refreshes the bearer token.
     */
    private suspend fun refreshBearer() {
        if (apiService.get() == null) {
            return
        }
        val loginBody = LoginBody(
            preference.getTag(BuildConfig.USER_ID),
            preference.getTag(BuildConfig.USER_PASSWORD)
        )

        val response = apiService.get().postLogin(loginBody)

        response.body()?.let {
            preference.setTag(BuildConfig.BEARER_TOKEN, it.token)
            preference.setTag(BuildConfig.EXPIRATION, it.expiration.toString())
        }
    }
}
