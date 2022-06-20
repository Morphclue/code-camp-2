package org.feature.fox.coffee_counter.di.services.network

import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.data.models.body.ItemBody
import org.feature.fox.coffee_counter.data.models.body.LoginBody
import org.feature.fox.coffee_counter.data.models.body.PurchaseBody
import org.feature.fox.coffee_counter.data.models.body.UserBody
import org.feature.fox.coffee_counter.data.models.response.*
import retrofit2.Response
import retrofit2.http.*


interface ApiService {


    @POST(BuildConfig.LOGIN_ENDPOINT)
    suspend fun postLogin(@Body loginBody: LoginBody): Response<LoginResponse>

    @POST(BuildConfig.USERS_ENDPOINT)
    suspend fun signUp(@Body userBody: UserBody): Response<String>

    @GET(BuildConfig.ITEMS_ENDPOINT)
    suspend fun getItems(): Response<List<ItemResponse>>

    @GET("${BuildConfig.ITEMS_ENDPOINT}/{id}")
    suspend fun getItemById(@Path("id") id: String): Response<ItemResponse>

    @GET(BuildConfig.USERS_ENDPOINT)
    suspend fun getUsers(): Response<List<UserResponse>>

    @GET("${BuildConfig.USERS_ENDPOINT}/{id}")
    suspend fun getUserById(
        @Header("Authorization") bearer: String,
        @Path("id") id: String,
    ): Response<UserIdResponse>

    @PUT("${BuildConfig.USERS_ENDPOINT}/{id}")
    suspend fun updateUser(
        @Header("Authorization") bearer: String,
        @Path("id") id: String,
        @Body userBody: UserBody,
    ): Response<String>

    @DELETE("${BuildConfig.USERS_ENDPOINT}/{id}")
    suspend fun deleteUser(
        @Header("Authorization") bearer: String,
        @Path("id") id: String,
    ): Response<String>

    @GET("${BuildConfig.USERS_ENDPOINT}/{id}/transactions")
    suspend fun getTransactions(
        @Header("Authorization") bearer: String,
        @Path("id") id: String,
    ): Response<List<TransactionResponse>>

    @POST("${BuildConfig.USERS_ENDPOINT}/{id}/purchases")
    suspend fun purchaseItem(
        @Header("Authorization") bearer: String,
        @Path("id") id: String,
        @Body purchaseBody: PurchaseBody,
    ): Response<String>

    @POST("${BuildConfig.USERS_ENDPOINT}/admin")
    suspend fun adminSignUp(
        @Header("Authorization") bearer: String,
        @Body userBody: UserBody,
    ): Response<String>

    @PUT("${BuildConfig.USERS_ENDPOINT}/admin/{id}")
    suspend fun updateAdmin(
        @Header("Authorization") bearer: String,
        @Path("id") id: String,
        @Body userBody: UserBody,
    ): Response<String>

    @POST("${BuildConfig.USERS_ENDPOINT}/{id}/funding")
    suspend fun addFunding(
        @Header("Authorization") bearer: String,
        @Path("id") id: String,
        @Body fundingBody: Double,
    ): Response<String>

    @POST(BuildConfig.ITEMS_ENDPOINT)
    suspend fun postItem(
        @Header("Authorization") bearer: String,
        @Body itemBody: ItemBody,
    ): Response<String>

    @PUT("${BuildConfig.ITEMS_ENDPOINT}/{id}")
    suspend fun updateItem(
        @Header("Authorization") bearer: String,
        @Path("id") id: String,
        @Body itemBody: ItemBody,
    ): Response<String>

    @DELETE("${BuildConfig.ITEMS_ENDPOINT}/{id}")
    suspend fun deleteItem(
        @Header("Authorization") bearer: String,
        @Path("id") id: String,
    ): Response<String>
}
