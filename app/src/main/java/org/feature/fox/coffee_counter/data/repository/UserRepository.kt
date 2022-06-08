package org.feature.fox.coffee_counter.data.repository

import org.feature.fox.coffee_counter.data.local.dao.UserDao
import org.feature.fox.coffee_counter.di.services.network.ApiService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val apiService: ApiService
)
