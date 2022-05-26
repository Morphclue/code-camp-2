package org.feature.fox.coffee_counter.data.models.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(

    @field:Json(name = "token")
    val token: String,

    @field:Json(name = "expiration")
    val expiration: Long
)
