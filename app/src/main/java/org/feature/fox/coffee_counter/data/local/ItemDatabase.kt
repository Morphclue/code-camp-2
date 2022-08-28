package org.feature.fox.coffee_counter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import org.feature.fox.coffee_counter.data.local.database.dao.ItemDao
import org.feature.fox.coffee_counter.data.local.database.tables.Item

/**
 * Declaration of the Item Database with its Entities according to RoomDB
 */
@Database(
    entities = [Item::class],
    version = 1
)
abstract class ItemDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}
