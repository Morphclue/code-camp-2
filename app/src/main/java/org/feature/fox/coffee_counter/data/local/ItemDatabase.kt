package org.feature.fox.coffee_counter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import org.feature.fox.coffee_counter.data.local.database.dao.ItemDao
import org.feature.fox.coffee_counter.data.local.database.tables.Item

@Database(
    entities = [Item::class],
    version = 1
)
abstract class ItemDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao
}
