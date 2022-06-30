package org.feature.fox.coffee_counter.data.repository

import androidx.lifecycle.LiveData
import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.data.local.database.dao.ItemDao
import org.feature.fox.coffee_counter.data.local.database.tables.Item
import org.feature.fox.coffee_counter.data.models.body.ItemBody
import org.feature.fox.coffee_counter.data.models.body.PurchaseBody
import org.feature.fox.coffee_counter.data.models.response.ItemResponse
import org.feature.fox.coffee_counter.di.services.AppPreference
import org.feature.fox.coffee_counter.di.services.network.ApiService
import org.feature.fox.coffee_counter.util.Resource
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val apiService: ApiService,
    private val preference: AppPreference,
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

    override suspend fun getItemByIdDb(itemId: String): Item {
        return itemDao.getItemById(itemId)
    }

    override fun observeAllItems(): LiveData<List<Item>> {
        return itemDao.observeAllItems()
    }

    override fun observeTotalPrice(): LiveData<Double> {
        return itemDao.observeTotalPrice()
    }

    override suspend fun postItem(itemBody: ItemBody): Resource<String> {
        return try {
            val bearerToken = preference.getTag(BuildConfig.BEARER_TOKEN)
            val response =
                apiService.postItem(bearerToken, itemBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Unknown error occured inside postItem method", null)
            } else {
                Resource.error("Unknown error occured inside postItem method", null)
            }
        } catch (e: Exception) {
            Resource.error("Could not reach the server.", null)
        }
    }

    override suspend fun getItems(): Resource<List<ItemResponse>> {
        return try {
            val response = apiService.getItems()
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Unknown error occured inside getItems method", null)
            } else {
                Resource.error("Unknown error occured inside getItems method", null)
            }
        } catch (e: Exception) {
            Resource.error("Could not reach the server.", null)
        }
    }

    override suspend fun getItemById(itemId: String): Resource<ItemResponse> {
        return try {
            val response = apiService.getItemById(itemId)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Unknown error occured inside getItemById method", null)
            } else {
                Resource.error("Unknown error occured inside getItemById method", null)
            }
        } catch (e: Exception) {
            Resource.error("Could not reach the server.", null)
        }
    }

    override suspend fun deleteItemById(itemId: String): Resource<String> {
        return try {
            val bearerToken = preference.getTag(BuildConfig.BEARER_TOKEN)
            val response =
                apiService.deleteItem(bearerToken, itemId)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Unknown error occured inside deleteItemById method", null)
            } else {
                Resource.error("Unknown error occured inside deleteItemById method", null)
            }
        } catch (e: Exception) {
            Resource.error("Could not reach the server.", null)
        }
    }

    override suspend fun updateItem(itemId: String, itemBody: ItemBody): Resource<String> {
        return try {
            val bearerToken = preference.getTag(BuildConfig.BEARER_TOKEN)
            val response = apiService.updateItem(bearerToken, itemId, itemBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Unknown error occured inside updateItem method", null)
            } else {
                Resource.error("Unknown error occured inside updateItem method", null)
            }
        } catch (e: Exception) {
            Resource.error("Could not reach the server.", null)
        }
    }

    override suspend fun purchaseItem(
        itemId: String,
        purchaseBody: PurchaseBody,
    ): Resource<String> {
        return try {
            val bearerToken = preference.getTag(BuildConfig.BEARER_TOKEN)
            val response =
                apiService.purchaseItem(bearerToken, itemId, purchaseBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Unknown error occured inside purchaseItem method", null)
            } else {
                Resource.error("Unknown error occured inside purchaseItem method", null)
            }
        } catch (e: Exception) {
            Resource.error("Could not reach the server.", null)
        }
    }
}
