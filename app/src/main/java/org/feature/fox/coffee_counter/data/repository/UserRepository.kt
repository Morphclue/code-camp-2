package org.feature.fox.coffee_counter.data.repository

import androidx.lifecycle.LiveData
import org.feature.fox.coffee_counter.data.local.dao.UserDao
import org.feature.fox.coffee_counter.data.local.database.tables.Funding
import org.feature.fox.coffee_counter.data.local.database.tables.Purchase
import org.feature.fox.coffee_counter.data.local.database.tables.User
import org.feature.fox.coffee_counter.data.models.body.LoginBody
import org.feature.fox.coffee_counter.data.models.body.PurchaseBody
import org.feature.fox.coffee_counter.data.models.body.UserBody
import org.feature.fox.coffee_counter.data.models.response.LoginResponse
import org.feature.fox.coffee_counter.data.models.response.TransactionResponse
import org.feature.fox.coffee_counter.data.models.response.UserIdResponse
import org.feature.fox.coffee_counter.data.models.response.UserResponse
import org.feature.fox.coffee_counter.di.services.network.ApiService
import retrofit2.Response
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

    override suspend fun postLogin(loginBody: LoginBody): Response<LoginResponse>{
        return apiService.postLogin(loginBody)
    }

    override suspend fun getUsers(): Response<List<UserResponse>>{
        return apiService.getUsers()
    }

    override suspend fun getUserById(id: String): Response<UserIdResponse>{
        return apiService.getUserById(id)
    }

    override suspend fun updateUser(id: String, userBody: UserBody): Response<String>{
        return apiService.updateUser(id, userBody)
    }

    override suspend fun signUp(userBody: UserBody): Response<String>{
        return apiService.signUp(userBody)
    }

    override suspend fun deleteUser(id: String): Response<String>{
        return apiService.deleteUser(id)
    }

    override suspend fun getTransactions(id: String): Response<List<TransactionResponse>>{
        return apiService.getTransactions(id)
    }

    override suspend fun purchaseItem(id: String, purchaseBody: PurchaseBody): Response<String>{
        return apiService.purchaseItem(id, purchaseBody)
    }

    override suspend fun adminSignUp(userBody: UserBody): Response<String>{
        return apiService.adminSignUp(userBody)
    }

    override suspend fun updateAdmin(id: String, userBody: UserBody): Response<String>{
        return apiService.updateAdmin(id, userBody)
    }

    override suspend fun addFunding(id: String, fundingBody: Double): Response<String>{
        return apiService.addFunding(id, fundingBody)
    }
}
