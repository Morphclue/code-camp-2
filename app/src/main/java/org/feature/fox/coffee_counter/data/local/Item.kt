package org.feature.fox.coffee_counter.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey
    val id: String,

    var name: String,

    var amount: Int = 0,

    var price: Double
)
