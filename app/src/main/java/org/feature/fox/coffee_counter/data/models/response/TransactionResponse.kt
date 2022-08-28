package org.feature.fox.coffee_counter.data.models.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Definition of the Moshi data class of a Transaction corresponding to the JSON Response.
 */
@JsonClass(generateAdapter = true)
data class TransactionResponse(
    @Json(name = "type")
    val type: String,

    @Json(name = "value")
    val value: Double,

    @Json(name = "timestamp")
    val timestamp: Long,

    @Json(name = "itemId")
    val itemId: String? = null,

    @Json(name = "itemName")
    val itemName: String? = null,

    @Json(name = "amount")
    val amount: Int? = null,
)
