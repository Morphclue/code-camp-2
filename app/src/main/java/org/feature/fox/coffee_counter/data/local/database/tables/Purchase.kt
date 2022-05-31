package org.feature.fox.coffee_counter.data.local.database.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "purchase",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("userId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
data class Purchase(

    @PrimaryKey
    val timestamp: Long,

    @ColumnInfo(index = true)
    val userId: String,

    val totalValue: Double,

    val itemId: String,

    val itemName: String,

    val amount: Int

)
