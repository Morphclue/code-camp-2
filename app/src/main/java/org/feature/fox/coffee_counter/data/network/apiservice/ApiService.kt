package org.feature.fox.coffee_counter.data.network.apiservice

import org.feature.fox.coffee_counter.data.models.body.LoginBody
import org.feature.fox.coffee_counter.data.models.body.UserBody
import org.feature.fox.coffee_counter.data.models.response.ItemResponse
import org.feature.fox.coffee_counter.data.models.response.LoginResponse
import org.feature.fox.coffee_counter.data.models.response.UserResponse
import org.feature.fox.coffee_counter.util.Constants.Companion.ITEMS_ENDPOINT
import org.feature.fox.coffee_counter.util.Constants.Companion.LOGIN_ENDPOINT
import org.feature.fox.coffee_counter.util.Constants.Companion.USERS_ENDPOINT
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService {

    @POST(LOGIN_ENDPOINT)
    suspend fun postLogin(@Body loginBody: LoginBody): Response<LoginResponse>

    @GET(USERS_ENDPOINT)
    suspend fun getUsers(): Response<List<UserResponse>>

    @GET(ITEMS_ENDPOINT)
    suspend fun getItems(): Response<List<ItemResponse>>

    @GET("$ITEMS_ENDPOINT/{id}")
    suspend fun getItemById(@Path("id") id: String): Response<ItemResponse>

    @POST(USERS_ENDPOINT)
    suspend fun signIn(@Body userBody: UserBody): Response<String>
}
