package org.feature.fox.coffee_counter.data.models.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Definition of the Moshi data class of an Item corresponding to the JSON Response.
 */
@JsonClass(generateAdapter = true)
data class ItemResponse(
    @Json(name = "id")
    val id: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "amount")
    val amount: Int,

    @Json(name = "price")
    val price: Double,
)
