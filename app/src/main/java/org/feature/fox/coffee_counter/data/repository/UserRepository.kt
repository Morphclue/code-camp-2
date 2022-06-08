package org.feature.fox.coffee_counter.data.repository

import androidx.lifecycle.LiveData
import org.feature.fox.coffee_counter.data.local.dao.UserDao
import org.feature.fox.coffee_counter.data.local.database.tables.Funding
import org.feature.fox.coffee_counter.data.local.database.tables.Purchase
import org.feature.fox.coffee_counter.data.local.database.tables.User
import org.feature.fox.coffee_counter.di.services.network.ApiService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val apiService: ApiService
) : UserRepositoryInt {
    override suspend fun insertUser(user: User){
        userDao.insertUser(user)
    }

    override suspend fun insertFunding(funding: Funding){
        userDao.insertFunding(funding)
    }

    override suspend fun insertPurchase(purchase: Purchase){
        userDao.insertPurchase(purchase)
    }

    override suspend fun deleteUser(user: User){
        userDao.deleteUser(user)
    }

    override suspend fun deleteFunding(funding: Funding){
        userDao.deleteFunding(funding)
    }

    override suspend fun deletePurchase(purchase: Purchase){
        userDao.deletePurchase(purchase)
    }

    override suspend fun getUserByIdDb(id: String): User{
        return userDao.getUserById(id)
    }

    override suspend fun getFundingListOfUser(id: String): List<Funding>{
        return userDao.getFundingListOfUser(id)
    }

    override suspend fun getPurchaseListOfUser(id: String): List<Purchase>{
        return userDao.getPurchaseListOfUser(id)
    }

    override fun observeTotalBalanceOfUser(id: String): LiveData<Double>{
        return userDao.observeTotalBalanceOfUser(id)
    }

    override suspend fun login(id: String, password: String): Boolean{
        return userDao.login(id, password)
    }

    override fun observeAllUsers(): LiveData<List<User>>{
        return userDao.observeAllUsers()
    }
}
