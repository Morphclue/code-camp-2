package org.feature.fox.coffee_counter.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import org.feature.fox.coffee_counter.data.local.database.tables.Item

/**
 * Implementation of the DAO for the Item database according to RoomDB.
 */
@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItemDb(item: Item)

    @Delete
    suspend fun deleteItemDb(item: Item)

    @Update
    suspend fun updateItemDb(item: Item)
}
