package org.feature.fox.coffee_counter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

import org.feature.fox.coffee_counter.data.local.dao.UserDao

@Database(
    entities = [User::class],
    version = 1
)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}