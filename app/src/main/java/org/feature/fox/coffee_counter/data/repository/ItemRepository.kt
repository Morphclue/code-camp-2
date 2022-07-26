package org.feature.fox.coffee_counter.data.repository

import androidx.lifecycle.LiveData
import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.data.local.database.dao.ItemDao
import org.feature.fox.coffee_counter.data.local.database.tables.Item
import org.feature.fox.coffee_counter.data.models.body.ItemBody
import org.feature.fox.coffee_counter.data.models.body.PurchaseBody
import org.feature.fox.coffee_counter.data.models.response.ItemResponse
import org.feature.fox.coffee_counter.di.services.network.ApiService
import org.feature.fox.coffee_counter.util.Resource
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val apiService: ApiService,
) : ItemRepositoryInt {

    override suspend fun insertItemDb(item: Item) {
        itemDao.insertItemDb(item)
    }

    override suspend fun deleteItemDb(item: Item) {
        itemDao.deleteItemDb(item)
    }

    override suspend fun updateItemDb(item: Item) {
        itemDao.updateItemDb(item)
    }

    override suspend fun getItemByIdDb(itemId: String): Item {
        TODO("Implement or delete me")
        //return itemDao.getItemById(itemId)
    }

    override fun observeAllItemsDb(): LiveData<List<Item>> {
        TODO("Implement or delete me")
        // return itemDao.observeAllItems()
    }

    override fun observeTotalPriceDb(): LiveData<Double> {
        TODO("Implement or delete me")
        // return itemDao.observeTotalPrice()
    }

    // API Calls

    override suspend fun postItem(itemBody: ItemBody): Resource<String> {
        return try {
            val response = apiService.postItem(itemBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(BuildConfig.UNKNOWN_ERROR, null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            Resource.error(BuildConfig.REACH_SERVER_ERROR, null)
        }
    }

    override suspend fun getItems(): Resource<List<ItemResponse>> {
        return try {
            val response = apiService.getItems()
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(BuildConfig.UNKNOWN_ERROR, null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            Resource.error(BuildConfig.REACH_SERVER_ERROR, null)
        }
    }

    override suspend fun getItemById(itemId: String): Resource<ItemResponse> {
        return try {
            val response = apiService.getItemById(itemId)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(BuildConfig.UNKNOWN_ERROR, null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            Resource.error(BuildConfig.REACH_SERVER_ERROR, null)
        }
    }

    override suspend fun deleteItemById(itemId: String): Resource<String> {
        return try {
            val response = apiService.deleteItem(itemId)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(BuildConfig.UNKNOWN_ERROR, null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            Resource.error(BuildConfig.REACH_SERVER_ERROR, null)
        }
    }

    override suspend fun updateItem(itemId: String, itemBody: ItemBody): Resource<String> {
        return try {
            val response = apiService.updateItem(itemId, itemBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(BuildConfig.UNKNOWN_ERROR, null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            Resource.error(BuildConfig.REACH_SERVER_ERROR, null)
        }
    }

    override suspend fun purchaseItem(
        userId: String,
        purchaseBody: PurchaseBody,
    ): Resource<String> {
        return try {
            val response = apiService.purchaseItem(userId, purchaseBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(BuildConfig.UNKNOWN_ERROR, null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            Resource.error(BuildConfig.REACH_SERVER_ERROR, null)
        }
    }
}
