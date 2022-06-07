package org.feature.fox.coffee_counter.data.repository

import androidx.lifecycle.LiveData
import org.feature.fox.coffee_counter.data.local.database.tables.Item
import org.feature.fox.coffee_counter.data.models.body.ItemBody
import org.feature.fox.coffee_counter.data.models.body.PurchaseBody
import org.feature.fox.coffee_counter.data.models.response.ItemResponse
import retrofit2.Response

interface ItemRepositoryInt {

    suspend fun insertItemDb(item: Item)

    suspend fun deleteItemDb(item: Item)

    suspend fun updateItemDb(item: Item)

    suspend fun getItemByIdDb(id: String): Item

    fun observeAllItems(): LiveData<List<Item>>

    fun observeTotalPrice(): LiveData<Double>

    suspend fun postItem(itemBody: ItemBody): Response<String>

    suspend fun getItems(): Response<List<ItemResponse>>

    suspend fun getItemByID(id: String): Response<ItemResponse>

    suspend fun updateItem(id: String, itemBody: ItemBody): Response<String>

    suspend fun deleteItemById(id: String): Response<String>

    suspend fun purchaseItem(itemId: String, purchaseBody: PurchaseBody): Response<String>
}
