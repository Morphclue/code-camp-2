package org.feature.fox.coffee_counter.data.local.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class Image(
    @PrimaryKey
    val userId: String,

    var encodedImage: String,

    var timestamp: Long,
)
