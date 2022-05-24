package org.feature.fox.coffee_counter.data.models.response

import com.squareup.moshi.Json

data class UserResponse(

    @Json(name="id")
    val id: String,

    @Json(name="name")
    val name: String
)
