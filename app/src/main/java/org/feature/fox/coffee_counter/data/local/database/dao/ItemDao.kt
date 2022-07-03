package org.feature.fox.coffee_counter.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.feature.fox.coffee_counter.data.local.database.tables.Item

@Dao
interface ItemDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)

    @Update
    suspend fun updateItem(item: Item)

    @Query("SELECT * FROM items WHERE id=:id")
    suspend fun getItemById(id: String): Item

    @Query("SELECT * FROM items")
    fun observeAllItems(): LiveData<List<Item>>

    @Query("SELECT SUM (price * amount) FROM items")
    fun observeTotalPrice(): LiveData<Double>
}
