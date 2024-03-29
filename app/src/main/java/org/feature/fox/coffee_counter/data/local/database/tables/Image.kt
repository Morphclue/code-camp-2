package org.feature.fox.coffee_counter.data.local.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Definition of the Image Entity according to RoomDB.
 */
@Entity(tableName = "image")
data class Image(
    @PrimaryKey
    val userId: String,

    var encodedImage: String,

    var timestamp: Long,
)
