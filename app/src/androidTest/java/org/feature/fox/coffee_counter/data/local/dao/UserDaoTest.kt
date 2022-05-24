package org.feature.fox.coffee_counter.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.feature.fox.coffee_counter.data.local.User
import org.feature.fox.coffee_counter.data.local.UserDatabase
import org.feature.fox.coffee_counter.getOrAwaitValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class UserDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: UserDatabase
    private lateinit var dao: UserDao

    @Before
    fun setUp(){
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UserDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.userDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertUser() = runTest{
        val user = User("98432kljfaf-34980fklaf", name = "Foo", isAdmin = false, password = "324987")
        dao.insertUser(user)

        val allUsers = dao.observeAllUsers().getOrAwaitValue()

        assertThat(allUsers.contains(user))
    }

    @Test
    fun deleteUser() = runTest{
        val user = User("98432kljfaf-34980fklaf", name = "Foo", isAdmin = false, password = "324987")
        dao.insertUser(user)
        dao.deleteUser(user)

        val allUsers = dao.observeAllUsers().getOrAwaitValue()


        assertThat(allUsers).doesNotContain(user)
    }

    @Test
    fun getUserById() = runTest {
        val user = User("98432kljfaf-34980fklaf", name = "Foo", isAdmin = false, password = "324987")
        dao.insertUser(user)

        val userById = dao.getUserById(user.id)

        assertThat(userById).isEqualTo(user)
    }

    @Test
    fun testLogin() = runTest {
        val user = User("98432kljfaf-34980fklaf", name = "Foo", isAdmin = false, password = "324987")
        dao.insertUser(user)

        // successful login
        assertThat(dao.login(user.id, user.password)).isEqualTo(true)

        // wrong password
        assertThat(dao.login(user.id, "")).isEqualTo(false)
        // wrong user id
        assertThat(dao.login("", user.password)).isEqualTo(false)


    }
}