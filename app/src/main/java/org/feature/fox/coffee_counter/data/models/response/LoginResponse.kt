package org.feature.fox.coffee_counter.data.models.response

import com.squareup.moshi.Json

data class LoginResponse(

    @Json(name="token")
    val token: String,

    @Json(name="expiration")
    val expiration: Long
)
