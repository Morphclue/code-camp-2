package org.feature.fox.coffee_counter.data.local.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Definition of the Achievement Entity according to RoomDB
 */
@Entity(tableName = "achievement")
data class Achievement(
    @PrimaryKey
    val name: String,

    val userId: String,

    val timestamp: Long,

    val description: String,

    val icon: Int,
)
