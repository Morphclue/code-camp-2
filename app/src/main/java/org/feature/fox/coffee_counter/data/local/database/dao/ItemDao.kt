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
    suspend fun insertItemDb(item: Item)

    @Delete
    suspend fun deleteItemDb(item: Item)

    @Update
    suspend fun updateItemDb(item: Item)

    //TODO Delete or use me
    @Query("SELECT * FROM items WHERE id=:id")
    suspend fun getItemByIdDb(id: String): Item

    //TODO Delete or use me
    @Query("SELECT * FROM items")
    fun observeAllItemsDb(): LiveData<List<Item>>

    //TODO Delete or use me
    @Query("SELECT SUM (price * amount) FROM items")
    fun observeTotalPriceDb(): LiveData<Double>
}
