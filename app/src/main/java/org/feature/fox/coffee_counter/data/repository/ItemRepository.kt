package org.feature.fox.coffee_counter.data.repository

import androidx.lifecycle.LiveData
import org.feature.fox.coffee_counter.data.local.dao.ItemDao
import org.feature.fox.coffee_counter.data.local.database.tables.Item
import org.feature.fox.coffee_counter.data.models.body.PurchaseBody
import org.feature.fox.coffee_counter.di.services.network.ApiService
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val apiService: ApiService
) : ItemRepositoryInt {

    override suspend fun insertItem(item: Item) {
        itemDao.insertItem(item)
    }

    override suspend fun deleteItem(item: Item) {
        itemDao.deleteItem(item)
    }

    override suspend fun updateItem(item: Item) {
        itemDao.updateItem(item)
    }

    override suspend fun getItemById(id: String): Item {
        return itemDao.getItemById(id)
    }

    override fun observeAllItems(): LiveData<List<Item>> {
        return itemDao.observeAllItems()
    }

    override fun observeTotalPrice(): LiveData<Double> {
        return itemDao.observeTotalPrice()
    }

    override suspend fun postItem(item: Item) {

    }

    override suspend fun purchaseItem(itemId: String, purchaseBody: PurchaseBody) {
        apiService.purchaseItem(itemId, purchaseBody)
    }
}
