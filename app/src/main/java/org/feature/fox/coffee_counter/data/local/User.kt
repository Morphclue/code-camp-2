package org.feature.fox.coffee_counter.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")

data class User(
    @PrimaryKey
    val id: String,

    var name: String,

    var isAdmin: Boolean,

    var password: String
)
