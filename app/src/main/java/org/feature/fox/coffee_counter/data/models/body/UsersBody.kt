package org.feature.fox.coffee_counter.data.models.body

import com.squareup.moshi.Json

data class UsersBody(

    @Json(name="id")
    val id: String,

    @Json(name="name")
    val name: String,

    @Json(name="password")
    val password: String,
)
