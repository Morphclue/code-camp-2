package org.feature.fox.coffee_counter.data.repository

import androidx.lifecycle.LiveData
import org.feature.fox.coffee_counter.data.local.database.tables.Item
import org.feature.fox.coffee_counter.data.models.body.PurchaseBody

interface ItemRepositoryInt {

    suspend fun insertItem(item: Item)

    suspend fun deleteItem(item: Item)

    suspend fun updateItem(item: Item)

    suspend fun getItemById(id: String): Item

    fun observeAllItems(): LiveData<List<Item>>

    fun observeTotalPrice(): LiveData<Double>

    suspend fun postItem(item: Item)

    suspend fun purchaseItem(itemId: String, purchaseBody: PurchaseBody)
}
