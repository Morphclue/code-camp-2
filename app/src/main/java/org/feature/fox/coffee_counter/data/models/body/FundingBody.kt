package org.feature.fox.coffee_counter.data.models.body

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Definition of the data class corresponding to the JSON Funding Body
 */
@JsonClass(generateAdapter = true)
data class FundingBody(
    @Json(name = "amount")
    val amount: Double,
)
