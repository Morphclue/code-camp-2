package org.feature.fox.coffee_counter.data.repository

import androidx.lifecycle.LiveData
import org.feature.fox.coffee_counter.data.local.database.dao.UserDao
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
import org.feature.fox.coffee_counter.util.Resource
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

    override suspend fun postLogin(loginBody: LoginBody): Resource<LoginResponse>{
        return try {
            val response = apiService.postLogin(loginBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Unknown error occurred.", null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            Resource.error("Could not reach the server.", null)
        }
    }

    override suspend fun getUsers(): Resource<List<UserResponse>>{
        return try {
            val response = apiService.getUsers()
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Unknown error occurred inside updateItem method", null)
            } else {
                Resource.error("Unknown error occurred inside updateItem method", null)
            }
        } catch (e: Exception) {
            Resource.error("Could not reach the server.", null)
        }
    }

    override suspend fun getUserById(bearer: String, id: String): Resource<UserIdResponse>{
        return try {
            val response = apiService.getUserById(bearer, id)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Unknown error occurred inside updateItem method", null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            Resource.error("Could not reach the server.", null)
        }
    }

    override suspend fun updateUser(bearer: String, id: String, userBody: UserBody): Resource<String>{
        return try {
            val response = apiService.updateUser(bearer, id, userBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Unknown error occurred inside updateItem method", null)
            } else {
                Resource.error("Unknown error occurred inside updateItem method", null)
            }
        } catch (e: Exception) {
            Resource.error("Could not reach the server.", null)
        }
    }

    override suspend fun signUp(userBody: UserBody): Resource<String>{
        return try {
            val response = apiService.signUp(userBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Unknown error occurred.", null)
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                Resource.error(errorMessage, null)
            }
        } catch (e: Exception) {
            Resource.error("Could not reach the server.", null)
        }
    }

    override suspend fun deleteUser(bearer: String, id: String): Resource<String>{
        return try {
            val response = apiService.deleteUser(bearer, id)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Unknown error occurred inside updateItem method", null)
            } else {
                Resource.error("Unknown error occurred inside updateItem method", null)
            }
        } catch (e: Exception) {
            Resource.error("Could not reach the server.", null)
        }
    }

    override suspend fun getTransactions(bearer: String, id: String): Resource<List<TransactionResponse>>{
        return try {
            val response = apiService.getTransactions(bearer, id)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Unknown error occurred inside updateItem method", null)
            } else {
                Resource.error("Unknown error occurred inside updateItem method", null)
            }
        } catch (e: Exception) {
            Resource.error("Could not reach the server.", null)
        }
    }

    override suspend fun purchaseItem(bearer: String, id: String, purchaseBody: PurchaseBody): Resource<String>{
        return try {
            val response = apiService.purchaseItem(bearer, id, purchaseBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Unknown error occurred inside updateItem method", null)
            } else {
                Resource.error("Unknown error occurred inside updateItem method", null)
            }
        } catch (e: Exception) {
            Resource.error("Could not reach the server.", null)
        }
    }

    override suspend fun adminSignUp(bearer: String, userBody: UserBody): Resource<String>{
        return try {
            val response = apiService.adminSignUp(bearer, userBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Unknown error occurred inside updateItem method", null)
            } else {
                Resource.error("Unknown error occurred inside updateItem method", null)
            }
        } catch (e: Exception) {
            Resource.error("Could not reach the server.", null)
        }
    }

    override suspend fun updateAdmin(bearer: String, id: String, userBody: UserBody): Resource<String>{
        return try {
            val response = apiService.updateAdmin(bearer, id, userBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Unknown error occurred inside updateItem method", null)
            } else {
                Resource.error("Unknown error occurred inside updateItem method", null)
            }
        } catch (e: Exception) {
            Resource.error("Could not reach the server.", null)
        }
    }

    override suspend fun addFunding(bearer: String, id: String, fundingBody: Double): Resource<String>{
        return try {
            val response = apiService.addFunding(bearer, id, fundingBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Unknown error occurred inside getItemById method", null)
            } else {
                Resource.error("Unknown error occurred inside getItemById method", null)
            }
        } catch (e: Exception) {
            Resource.error("Could not reach the server.", null)
        }
    }
}
