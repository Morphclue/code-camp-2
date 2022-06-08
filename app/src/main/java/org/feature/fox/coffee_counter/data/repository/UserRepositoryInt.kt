package org.feature.fox.coffee_counter.data.repository

import androidx.lifecycle.LiveData
import org.feature.fox.coffee_counter.data.local.database.tables.Funding
import org.feature.fox.coffee_counter.data.local.database.tables.Purchase
import org.feature.fox.coffee_counter.data.local.database.tables.User

interface UserRepositoryInt{

    suspend fun insertUser(user: User)

    suspend fun insertFunding(funding: Funding)

    suspend fun insertPurchase(purchase: Purchase)

    suspend fun deleteUser(user: User)

    suspend fun deleteFunding(funding: Funding)

    suspend fun deletePurchase(purchase: Purchase)

    suspend fun getUserById(id: String): User

    suspend fun getFundingListOfUser(id: String): List<Funding>

    suspend fun getPurchaseListOfUser(id: String): List<Purchase>

    fun observeTotalBalanceOfUser(id: String): LiveData<Double>

    suspend fun login(id: String, password: String): Boolean

    fun observeAllUsers(): LiveData<List<User>>
}
