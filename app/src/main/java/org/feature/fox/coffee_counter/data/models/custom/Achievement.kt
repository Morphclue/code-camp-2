package org.feature.fox.coffee_counter.data.models.custom

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Achievement(
    @Json(name = "name")
    val name: String,

    @Json(name = "timestamp")
    val timestamp: Long,

    @Json(name = "description")
    val description: String,
)