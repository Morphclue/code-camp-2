package org.feature.fox.coffee_counter.data.models.body

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Definition of the data class corresponding to the JSON User Body.
 */

@JsonClass(generateAdapter = true)
data class UserBody(
    @Json(name = "id")
    val id: String? = null,

    @Json(name = "name")
    val name: String,

    @Json(name = "password")
    val password: String? = null,

    @Json(name = "isAdmin")
    val isAdmin: Boolean? = null,
)
