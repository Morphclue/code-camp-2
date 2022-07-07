package org.feature.fox.coffee_counter.data.repository

import androidx.lifecycle.LiveData
import org.feature.fox.coffee_counter.data.local.database.tables.Funding
import org.feature.fox.coffee_counter.data.local.database.tables.Purchase
import org.feature.fox.coffee_counter.data.local.database.tables.User
import org.feature.fox.coffee_counter.data.models.body.FundingBody
import org.feature.fox.coffee_counter.data.models.body.LoginBody
import org.feature.fox.coffee_counter.data.models.body.PurchaseBody
import org.feature.fox.coffee_counter.data.models.body.UserBody
import org.feature.fox.coffee_counter.data.models.response.LoginResponse
import org.feature.fox.coffee_counter.data.models.response.TransactionResponse
import org.feature.fox.coffee_counter.data.models.response.UserIdResponse
import org.feature.fox.coffee_counter.data.models.response.UserResponse
import org.feature.fox.coffee_counter.util.Resource

interface UserRepositoryInt {
    suspend fun insertUser(user: User)

    suspend fun insertFunding(funding: Funding)

    suspend fun insertPurchase(purchase: Purchase)

    suspend fun deleteUser(user: User)

    suspend fun deleteFunding(funding: Funding)

    suspend fun deletePurchase(purchase: Purchase)

    suspend fun getUserByIdDb(id: String): User

    suspend fun getFundingListOfUser(id: String): List<Funding>

    suspend fun getPurchaseListOfUser(id: String): List<Purchase>

    fun observeTotalBalanceOfUser(id: String): LiveData<Double>

    suspend fun login(id: String, password: String): Boolean

    fun observeAllUsers(): LiveData<List<User>>

    suspend fun postLogin(loginBody: LoginBody): Resource<LoginResponse>

    suspend fun getUsers(): Resource<List<UserResponse>>

    suspend fun getUserById(id: String): Resource<UserIdResponse>

    suspend fun updateUser(id: String, userBody: UserBody): Resource<String>

    suspend fun signUp(userBody: UserBody): Resource<String>

    suspend fun deleteUser(id: String): Resource<String>

    suspend fun getTransactions(id: String): Resource<List<TransactionResponse>>

    suspend fun purchaseItem(id: String, purchaseBody: PurchaseBody): Resource<String>

    suspend fun adminSignUp(userBody: UserBody): Resource<String>

    suspend fun updateAdmin(id: String, userBody: UserBody): Resource<String>

    suspend fun addFunding(id: String, fundingBody: FundingBody): Resource<String>
}
