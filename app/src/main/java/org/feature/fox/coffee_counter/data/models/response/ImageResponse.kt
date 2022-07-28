package org.feature.fox.coffee_counter.data.models.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageResponse(
    @Json(name = "encodedImage")
    val encodedImage: String,

    @Json(name = "timestamp")
    val timestamp: Long,
)
