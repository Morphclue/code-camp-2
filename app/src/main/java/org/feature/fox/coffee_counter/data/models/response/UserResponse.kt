package org.feature.fox.coffee_counter.data.models.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponse(

    @field:Json(name = "id")
    val id: String,

    @field:Json(name = "name")
    val name: String
)
