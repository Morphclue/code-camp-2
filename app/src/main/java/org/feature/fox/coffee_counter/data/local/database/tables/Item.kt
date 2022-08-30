package org.feature.fox.coffee_counter.data.local.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Definition of the Item Entity according to RoomDB.
 */
@Entity(tableName = "items")
data class Item(
    @PrimaryKey
    val id: String,

    var name: String,

    var amount: Int = 0,

    var price: Double,
)
