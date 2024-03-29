package org.feature.fox.coffee_counter.data.repository

import org.feature.fox.coffee_counter.data.local.database.tables.Achievement
import org.feature.fox.coffee_counter.data.local.database.tables.Funding
import org.feature.fox.coffee_counter.data.local.database.tables.Image
import org.feature.fox.coffee_counter.data.local.database.tables.Purchase
import org.feature.fox.coffee_counter.data.local.database.tables.User
import org.feature.fox.coffee_counter.data.models.body.FundingBody
import org.feature.fox.coffee_counter.data.models.body.LoginBody
import org.feature.fox.coffee_counter.data.models.body.PurchaseBody
import org.feature.fox.coffee_counter.data.models.body.SendMoneyBody
import org.feature.fox.coffee_counter.data.models.body.UserBody
import org.feature.fox.coffee_counter.data.models.response.LoginResponse
import org.feature.fox.coffee_counter.data.models.response.TransactionResponse
import org.feature.fox.coffee_counter.data.models.response.UserIdResponse
import org.feature.fox.coffee_counter.data.models.response.UserResponse
import org.feature.fox.coffee_counter.util.Resource
import java.io.InputStream

/**
 * Interface for the User Repository combining access to both User-related RoomDB queries and API Calls.
 */
interface IUserRepository {
    suspend fun insertUserDb(user: User)

    suspend fun insertFundingDb(funding: Funding)

    suspend fun insertPurchaseDb(purchase: Purchase)

    suspend fun insertImageDb(image: Image)

    suspend fun insertAchievementDb(achievement: Achievement)

    suspend fun updateUserDb(user: User)

    suspend fun getAdminStateOfUserByIdDb(userId: String): Boolean

    suspend fun getImageByIdDb(id: String): Image?

    suspend fun getPurchaseListOfUserDb(userId: String): List<Purchase>

    suspend fun getAchievementListOfUserDb(userId: String): List<Achievement>

    suspend fun getFundingOfUserByIdDb(userId: String): List<Funding>

    suspend fun deleteUserDb(user: User)

    suspend fun deleteImageDb(image: Image)

    //================================================================================
    // API CALLS - PROVIDE RESOURCES
    //================================================================================

    suspend fun postLogin(loginBody: LoginBody): Resource<LoginResponse>

    suspend fun getUsers(): Resource<List<UserResponse>>

    suspend fun getUsersAsAdmin(): Resource<List<UserResponse>>

    suspend fun getUserById(id: String): Resource<UserIdResponse>

    suspend fun updateUser(id: String, userBody: UserBody): Resource<String>

    suspend fun signUp(userBody: UserBody): Resource<String>

    suspend fun deleteUser(id: String): Resource<String>

    suspend fun getTransactions(id: String): Resource<List<TransactionResponse>>

    suspend fun purchaseItem(id: String, purchaseBody: PurchaseBody): Resource<String>

    suspend fun sendMoney(id: String, sendMoneyBody: SendMoneyBody): Resource<String>

    suspend fun getImage(id: String): Resource<Image>

    suspend fun uploadImage(id: String, inputStream: InputStream): Resource<String>

    suspend fun getImageTimestamp(id: String): Resource<Long>

    suspend fun adminSignUp(userBody: UserBody): Resource<String>

    suspend fun updateAdmin(id: String, userBody: UserBody): Resource<String>

    suspend fun addFunding(id: String, fundingBody: FundingBody): Resource<String>
}
