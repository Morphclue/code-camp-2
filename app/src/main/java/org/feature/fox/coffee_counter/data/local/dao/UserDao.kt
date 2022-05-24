package org.feature.fox.coffee_counter.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.feature.fox.coffee_counter.data.local.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser (user: User)

    @Delete
    suspend fun deleteUser (user: User)

    @Query("SELECT * FROM users")
    fun observeAllUsers(): LiveData<List<User>>

}