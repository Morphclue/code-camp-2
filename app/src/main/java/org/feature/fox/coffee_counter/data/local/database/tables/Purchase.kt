package org.feature.fox.coffee_counter.data.local.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "purchase")
data class Purchase(

    @PrimaryKey
    val timestamp: Long,

    val userId: String,

    val totalValue: Double,

    val itemId: String,

    val itemName: String,

    val amount: Int

)
