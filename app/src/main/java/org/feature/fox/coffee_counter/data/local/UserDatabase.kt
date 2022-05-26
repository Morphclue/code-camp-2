package org.feature.fox.coffee_counter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import org.feature.fox.coffee_counter.data.local.dao.UserDao
import org.feature.fox.coffee_counter.data.local.database.tables.Funding
import org.feature.fox.coffee_counter.data.local.database.tables.User

@Database(
    entities = [User::class, Funding::class],
    version = 1
)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}
