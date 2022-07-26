package org.feature.fox.coffee_counter.data.repository

import androidx.lifecycle.LiveData
import org.feature.fox.coffee_counter.data.local.database.relations.FundingsOfUser
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

    suspend fun insertUserDb(user: User)

    suspend fun updateUserDb(user: User)

    suspend fun getAdminStateOfUserByIdDb(userId: String): Boolean

    suspend fun insertFundingDb(funding: Funding)

    suspend fun insertPurchaseDb(purchase: Purchase)

    suspend fun deleteUserDb(user: User)

    //TODO Delete or use me
    suspend fun deleteFundingDb(funding: Funding)

    //TODO Delete or use me
    suspend fun deletePurchaseDb(purchase: Purchase)

    //TODO Delete or use me
    suspend fun getUserByIdDb(id: String): User

    //TODO Delete or use me
    suspend fun getFundingListOfUserDb(id: String): FundingsOfUser

    //TODO Delete or use me
    suspend fun getPurchaseListOfUserDb(id: String): List<Purchase>

    //TODO Delete or use me
    fun observeTotalBalanceOfUserDb(id: String): LiveData<Double>

    //TODO Delete or use me
    fun observeAllUsersDb(): LiveData<List<User>>

    // API Calls

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
