package org.feature.fox.coffee_counter.data.models.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransactionResponse(
    @Json(name = "type")
    val type: String,

    @Json(name = "value")
    val value: Double,

    @Json(name = "balance")
    val timestamp: Long,

    @Json(name = "itemId")
    val itemId: String,

    @Json(name = "itemName")
    val itemName: String,

    @Json(name = "amount")
    val amount: Int,
)
