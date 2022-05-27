package org.feature.fox.coffee_counter.di.services.network

import org.feature.fox.coffee_counter.data.models.body.LoginBody
import org.feature.fox.coffee_counter.data.models.body.UserBody
import org.feature.fox.coffee_counter.data.models.response.*
import org.feature.fox.coffee_counter.util.Constants.Companion.ITEMS_ENDPOINT
import org.feature.fox.coffee_counter.util.Constants.Companion.LOGIN_ENDPOINT
import org.feature.fox.coffee_counter.util.Constants.Companion.USERS_ENDPOINT
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @POST(LOGIN_ENDPOINT)
    suspend fun postLogin(@Body loginBody: LoginBody): Response<LoginResponse>

    @GET(ITEMS_ENDPOINT)
    suspend fun getItems(): Response<List<ItemResponse>>

    @GET("$ITEMS_ENDPOINT/{id}")
    suspend fun getItemById(@Path("id") id: String): Response<ItemResponse>

    @GET(USERS_ENDPOINT)
    suspend fun getUsers(): Response<List<UserResponse>>

    @GET("$USERS_ENDPOINT/{id}")
    suspend fun getUserById(@Path("id") id: String): Response<UserIdResponse>

    // TODO: Currently not working (tested in Postman)
    @PUT(USERS_ENDPOINT)
    suspend fun updateUser(@Body userBody: UserBody)

    @POST(USERS_ENDPOINT)
    suspend fun signIn(@Body userBody: UserBody): Response<String>

    @DELETE("$USERS_ENDPOINT/{id}")
    suspend fun deleteUser(@Path("id") id: String): Response<String>

    @GET("$USERS_ENDPOINT/{id}/transactions")
    suspend fun getTransactions(@Path("id") id: String): Response<List<TransactionResponse>>

    @POST("$USERS_ENDPOINT/{id}/purchases")
    suspend fun purchaseItem(@Path("id") id: String): Response<String>
}
