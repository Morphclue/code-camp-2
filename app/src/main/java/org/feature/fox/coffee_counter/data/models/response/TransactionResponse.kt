package org.feature.fox.coffee_counter.data.models.response

import com.squareup.moshi.Json

data class TransactionResponse(
    @field:Json(name = "type")
    val type: String,

    @field:Json(name = "value")
    val value: Double,

    @field:Json(name = "balance")
    val timestamp: Long,

    @field:Json(name = "itemId")
    val itemId: String,

    @field:Json(name = "itemName")
    val itemName: String,

    @field:Json(name = "amount")
    val amount: Int,
)
