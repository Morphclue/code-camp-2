package org.feature.fox.coffee_counter.data.models.body

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Definition of the data class corresponding to the JSON Item Body.
 */

@JsonClass(generateAdapter = true)
class ItemBody(
    @Json(name = "id")
    val id: String? = null,

    @Json(name = "name")
    val name: String,

    @Json(name = "amount")
    val amount: Int,

    @Json(name = "price")
    val price: Double,
)
