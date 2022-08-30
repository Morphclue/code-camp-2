package org.feature.fox.coffee_counter.data.models.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Definition of the Moshi data class of a User corresponding to the JSON Response.
 */
@JsonClass(generateAdapter = true)
data class UserIdResponse(
    @Json(name = "id")
    val id: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "balance")
    val balance: Double,
)
