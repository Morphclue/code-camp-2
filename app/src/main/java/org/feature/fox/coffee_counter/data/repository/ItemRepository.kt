package org.feature.fox.coffee_counter.data.repository

import org.feature.fox.coffee_counter.data.local.dao.ItemDao
import org.feature.fox.coffee_counter.di.services.network.ApiService
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val apiService: ApiService
) {
}
