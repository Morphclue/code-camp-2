package org.feature.fox.coffee_counter.data.models.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserIdResponse(
    @field:Json(name = "id")
    val id: String,

    @field:Json(name = "name")
    val name: String,

    @field:Json(name = "balance")
    val balance: Double,
)
