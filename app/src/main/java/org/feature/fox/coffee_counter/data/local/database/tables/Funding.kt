package org.feature.fox.coffee_counter.data.local.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Definition of the Funding Entity according to RoomDB.
 */
@Entity(tableName = "funding")
data class Funding(
    @PrimaryKey
    val timestamp: Long,

    val userId: String,

    val value: Double,
)
