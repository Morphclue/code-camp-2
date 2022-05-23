package org.feature.fox.coffee_counter.data.network.apiservice

import org.feature.fox.coffee_counter.data.models.body.LoginBody
import org.feature.fox.coffee_counter.data.models.body.UsersBody
import org.feature.fox.coffee_counter.data.models.response.ItemResponse
import org.feature.fox.coffee_counter.data.models.response.UserResponse
import org.feature.fox.coffee_counter.data.models.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {

    @POST("/login")
    suspend fun postLogin(@Body loginBody: LoginBody): Response<LoginResponse>

    @GET("/users")
    suspend fun getUsers(): Response<List<UserResponse>>

    @GET("/items")
    suspend fun getItems(): Response<List<ItemResponse>>

    @GET("/items/{id}")
    suspend fun getItemById(): Response<ItemResponse>

    @POST("/users")
    suspend fun signIn(@Body usersBody: UsersBody): Response<String>
}