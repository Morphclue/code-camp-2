package org.feature.fox.coffee_counter.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.feature.fox.coffee_counter.data.local.Item
import org.feature.fox.coffee_counter.data.local.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser (user: User)

    @Delete
    suspend fun deleteUser (user: User)

    @Query("SELECT * FROM users WHERE id=:id")
    suspend fun getUserById(id: String): User

    //@Query("SELECT * FROM users WHERE EXISTS (SELECT id, password FROM users WHERE id=:id AND password=:password)")
    @Query("SELECT CASE WHEN EXISTS (SELECT * FROM users WHERE id=:id AND password=:password) THEN CAST(1 AS BOOLEAN) ELSE CAST(0 AS BOOLEAN) END")
    suspend fun login(id: String, password: String): Boolean

    @Query("SELECT * FROM users")
    fun observeAllUsers(): LiveData<List<User>>


}