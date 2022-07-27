package org.feature.fox.coffee_counter.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import androidx.room.Update
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

    @Transaction
    @Query("SELECT * FROM purchase WHERE userId = :userId")
    suspend fun getPurchasesOfUserByIdDb(userId: String): List<Purchase>

}
