package org.feature.fox.coffee_counter.data.models.response

import com.squareup.moshi.Json

data class ItemResponse(
    @Json(name="id")
    val id: String,

    @Json(name="name")
    val name: String,

    @Json(name="amount")
    val amount: Int,

    @Json(name="price")
    val price: Double
)




