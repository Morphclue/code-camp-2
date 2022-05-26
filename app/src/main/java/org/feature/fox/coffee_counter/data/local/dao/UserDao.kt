package org.feature.fox.coffee_counter.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.feature.fox.coffee_counter.data.local.database.relations.FundingAndUser
import org.feature.fox.coffee_counter.data.local.database.tables.Funding
import org.feature.fox.coffee_counter.data.local.database.tables.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFunding(funding: List<Funding>)

    @Delete
    suspend fun deleteUser(user: User)

    @Delete
    suspend fun deleteFunding(funding: Funding)

    @Query("SELECT * FROM users WHERE id=:id")
    suspend fun getUserById(id: String): User

    @Transaction
    @Query("SELECT * FROM users WHERE id=:id")
    suspend fun getFundingsOfUser(id: String): List<FundingAndUser>

    @Query("SELECT CASE WHEN EXISTS (SELECT * FROM users WHERE id=:id AND password=:password) THEN CAST(1 AS BOOLEAN) ELSE CAST(0 AS BOOLEAN) END")
    suspend fun login(id: String, password: String): Boolean

    @Query("SELECT * FROM users")
    fun observeAllUsers(): LiveData<List<User>>




}
