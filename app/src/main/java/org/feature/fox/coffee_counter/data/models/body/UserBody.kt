package org.feature.fox.coffee_counter.data.models.body

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserBody(

    @field:Json(name = "id")
    val id: String,

    @field:Json(name = "name")
    val name: String,

    @field:Json(name = "password")
    val password: String,
)
