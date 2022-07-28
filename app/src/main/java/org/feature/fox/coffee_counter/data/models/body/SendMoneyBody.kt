package org.feature.fox.coffee_counter.data.models.body

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SendMoneyBody(
    @Json(name = "amount")
    val amount: Double,

    @Json(name = "recipientId")
    val recipientId: String,
)
