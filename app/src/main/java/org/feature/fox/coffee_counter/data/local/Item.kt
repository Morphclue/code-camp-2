package org.feature.fox.coffee_counter.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: String? = null,
    var name: String,
    var amount:Int,
    var price: Double
)
