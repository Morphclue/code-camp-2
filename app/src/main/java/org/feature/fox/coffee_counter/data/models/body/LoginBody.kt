package org.feature.fox.coffee_counter.data.models.body

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Definition of the data class corresponding to the JSON Login Body.
 */

@JsonClass(generateAdapter = true)
data class LoginBody(
    @Json(name = "id")
    val id: String,

    @Json(name = "password")
    val password: String,
)
