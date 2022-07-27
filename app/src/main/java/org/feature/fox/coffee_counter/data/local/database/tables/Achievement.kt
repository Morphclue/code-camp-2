package org.feature.fox.coffee_counter.data.local.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievement")
data class Achievement(
    @PrimaryKey
    val name: String,

    val userId: String,

    val timestamp: Long,

    val description: String,

    val received: Boolean,
)