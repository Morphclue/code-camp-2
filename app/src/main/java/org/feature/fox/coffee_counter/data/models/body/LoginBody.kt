package org.feature.fox.coffee_counter.data.models.body

import com.squareup.moshi.Json

data class LoginBody(
    @Json(name="id")
    val id: String,

    @Json(name="password")
    val password: String,
)
