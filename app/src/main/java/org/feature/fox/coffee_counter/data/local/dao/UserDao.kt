package org.feature.fox.coffee_counter.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.feature.fox.coffee_counter.data.local.database.tables.Funding
import org.feature.fox.coffee_counter.data.local.database.tables.Purchase
import org.feature.fox.coffee_counter.data.local.database.tables.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFunding(funding: Funding)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchase(purchase: List<Purchase>)

    @Delete
    suspend fun deleteUser(user: User)

    @Delete
    suspend fun deleteFunding(funding: Funding)

    @Delete
    suspend fun deletePurchase(purchase: Purchase)

    @Query("SELECT * FROM users WHERE id=:id")
    suspend fun getUserById(id: String): User

    @Transaction
    @Query("SELECT * FROM funding WHERE userId=:id")
    suspend fun getFundingListOfUser(id: String): List<Funding>

    @Query("SELECT CASE WHEN EXISTS (SELECT * FROM users WHERE id=:id AND password=:password) THEN CAST(1 AS BOOLEAN) ELSE CAST(0 AS BOOLEAN) END")
    suspend fun login(id: String, password: String): Boolean

    @Query("SELECT * FROM users")
    fun observeAllUsers(): LiveData<List<User>>




}
