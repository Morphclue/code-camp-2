package org.feature.fox.coffee_counter.data.repository

import androidx.lifecycle.LiveData
import org.feature.fox.coffee_counter.data.local.database.tables.Item
import org.feature.fox.coffee_counter.data.models.body.ItemBody
import org.feature.fox.coffee_counter.data.models.body.PurchaseBody
import org.feature.fox.coffee_counter.data.models.response.ItemResponse
import org.feature.fox.coffee_counter.util.Resource

interface ItemRepositoryInt {

    suspend fun insertItemDb(item: Item)

    suspend fun deleteItemDb(item: Item)

    suspend fun updateItemDb(item: Item)

    // TODO Use or delete me
    suspend fun getItemByIdDb(itemId: String): Item

    // TODO Use or delete me
    fun observeAllItemsDb(): LiveData<List<Item>>

    // TODO Use or delete me
    fun observeTotalPriceDb(): LiveData<Double>

    // API Calls

    suspend fun postItem(itemBody: ItemBody): Resource<String>

    suspend fun getItems(): Resource<List<ItemResponse>>

    suspend fun getItemById(itemId: String): Resource<ItemResponse>

    suspend fun updateItem(itemId: String, itemBody: ItemBody): Resource<String>

    suspend fun deleteItemById(itemId: String): Resource<String>

    suspend fun purchaseItem(itemId: String, purchaseBody: PurchaseBody): Resource<String>
}
