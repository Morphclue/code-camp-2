package org.feature.fox.coffee_counter.data.repository

import androidx.lifecycle.LiveData
import org.feature.fox.coffee_counter.data.local.dao.ItemDao
import org.feature.fox.coffee_counter.data.local.database.tables.Item
import org.feature.fox.coffee_counter.data.models.body.ItemBody
import org.feature.fox.coffee_counter.data.models.body.PurchaseBody
import org.feature.fox.coffee_counter.data.models.response.ItemResponse
import org.feature.fox.coffee_counter.di.services.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val apiService: ApiService
) : ItemRepositoryInt {

    override suspend fun insertItemDb(item: Item) {
        itemDao.insertItem(item)
    }

    override suspend fun deleteItemDb(item: Item) {
        itemDao.deleteItem(item)
    }

    override suspend fun updateItemDb(item: Item) {
        itemDao.updateItem(item)
    }


    override suspend fun getItemByIdDb(id: String): Item {
        return itemDao.getItemById(id)
    }

    override fun observeAllItems(): LiveData<List<Item>> {
        return itemDao.observeAllItems()
    }

    override fun observeTotalPrice(): LiveData<Double> {
        return itemDao.observeTotalPrice()
    }

    override suspend fun postItem(itemBody: ItemBody): Response<String> {
        return apiService.postItem(itemBody)
    }

    override suspend fun getItems(): Response<List<ItemResponse>> {
        return apiService.getItems()
    }

    override suspend fun getItemByID(id: String): Response<ItemResponse> {
        return apiService.getItemById(id)
    }

    override suspend fun deleteItemById(id: String): Response<String> {
        return apiService.deleteItem(id)
    }

    override suspend fun updateItem(id: String, itemBody: ItemBody): Response<String> {
        return apiService.updateItem(id, itemBody)
    }

    override suspend fun purchaseItem(
        itemId: String,
        purchaseBody: PurchaseBody
    ): Response<String> {
        return apiService.purchaseItem(itemId, purchaseBody)
    }
}
