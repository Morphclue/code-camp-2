package org.feature.fox.coffee_counter.data.models.response

data class LoginResponse(
    val token: String,

    // Unix Timestamp, expires after 5 minutes
    val expiration: Long
)
