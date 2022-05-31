package org.feature.fox.coffee_counter.data.models.body

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PurchaseBody(

    @Json(name = "itemId")
    val itemId: String,

    @Json(name = "amount")
    val amount: Int,
) {
}
