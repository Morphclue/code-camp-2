package org.feature.fox.coffee_counter.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.feature.fox.coffee_counter.data.local.Item

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem (item: Item)

    @Delete
    suspend fun deleteItem (item: Item)

    @Query("SELECT * FROM items")
    fun observeAllItems(): LiveData<List<Item>>

    @Query("SELECT SUM (price * amount) FROM items")
    fun observeTotalPrice(): LiveData<Double>
}