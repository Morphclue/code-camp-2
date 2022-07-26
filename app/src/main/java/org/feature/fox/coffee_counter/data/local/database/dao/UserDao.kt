package org.feature.fox.coffee_counter.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import androidx.room.Update
import org.feature.fox.coffee_counter.data.local.database.relations.FundingsOfUser
import org.feature.fox.coffee_counter.data.local.database.tables.Funding
import org.feature.fox.coffee_counter.data.local.database.tables.Purchase
import org.feature.fox.coffee_counter.data.local.database.tables.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserDb(user: User)

    @Update
    suspend fun updateUserDb(user: User)

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT isAdmin FROM users WHERE userId = :userId")
    suspend fun getAdminStateOfUserByIdDb(userId: String): Boolean

    @Delete
    suspend fun deleteUserDb(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFundingDb(funding: Funding)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPurchaseDb(purchase: Purchase)


    //TODO Delete or use me
    @Delete
    suspend fun deleteFundingDb(funding: Funding)

    //TODO Delete or use me
    @Delete
    suspend fun deletePurchaseDb(purchase: Purchase)

    //TODO Delete or use me
    @Query("SELECT * FROM users WHERE userId=:userId")
    suspend fun getUserByIdDb(userId: String): User

    //TODO Delete or use me
    @Transaction
    @Query("SELECT * FROM users")
    suspend fun getFundingListOfUserDb(): List<FundingsOfUser>

    //TODO Delete or use me
    @Transaction
    @Query("SELECT * FROM purchase WHERE userId=:id")
    suspend fun getPurchaseListOfUserDb(id: String): List<Purchase>

    //TODO Delete or use me
    @Transaction
    @Query("SELECT(SELECT SUM(funding.value) FROM funding WHERE userId=:id) + (SELECT SUM(purchase.totalValue) FROM purchase WHERE userId=:id)")
    fun observeTotalBalanceOfUserDb(id: String): LiveData<Double>

    //TODO Delete or use me
    @Query("SELECT * FROM users")
    fun observeAllUsersDb(): LiveData<List<User>>
}
