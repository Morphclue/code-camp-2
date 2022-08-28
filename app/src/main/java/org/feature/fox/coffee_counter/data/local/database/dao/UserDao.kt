package org.feature.fox.coffee_counter.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import androidx.room.Update
import org.feature.fox.coffee_counter.data.local.database.tables.Achievement
import org.feature.fox.coffee_counter.data.local.database.tables.Funding
import org.feature.fox.coffee_counter.data.local.database.tables.Image
import org.feature.fox.coffee_counter.data.local.database.tables.Purchase
import org.feature.fox.coffee_counter.data.local.database.tables.User

/**
 * Implementation of the DAO for the User database according to RoomDB
 */
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserDb(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFundingDb(funding: Funding)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPurchaseDb(purchase: Purchase)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImageDb(image: Image)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAchievementDb(achievement: Achievement)

    @Update
    suspend fun updateUserDb(user: User)

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT isAdmin FROM users WHERE userId = :userId")
    suspend fun getAdminStateOfUserByIdDb(userId: String): Boolean

    @Query("SELECT * FROM image WHERE userId=:id")
    suspend fun getImageByIdDb(id: String): Image?

    @Transaction
    @Query("SELECT * FROM funding WHERE userId = :userId")
    suspend fun getFundingOfUserByIdDb(userId: String): List<Funding>

    @Transaction
    @Query("SELECT * FROM purchase WHERE userId=:userId")
    suspend fun getPurchaseListOfUserDb(userId: String): List<Purchase>

    @Transaction
    @Query("SELECT * FROM achievement WHERE userId=:userId")
    suspend fun getAchievementListOfUserDb(userId: String): List<Achievement>

    @Delete
    suspend fun deleteUserDb(user: User)

    @Delete
    suspend fun deleteImageDb(image: Image)
}
