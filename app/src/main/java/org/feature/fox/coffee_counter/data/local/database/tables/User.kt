package org.feature.fox.coffee_counter.data.local.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Definition of the User Entity according to RoomDB.
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val userId: String,

    var name: String,

    var isAdmin: Boolean,
)


