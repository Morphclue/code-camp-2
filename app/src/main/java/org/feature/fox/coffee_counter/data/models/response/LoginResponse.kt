package org.feature.fox.coffee_counter.data.models.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Definition of the Moshi data class of an Authentication Token corresponding to the JSON Response.
 */
@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "token")
    val token: String,

    @Json(name = "expiration")
    val expiration: Long,
)
