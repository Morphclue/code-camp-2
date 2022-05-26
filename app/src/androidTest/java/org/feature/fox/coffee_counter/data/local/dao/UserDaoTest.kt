package org.feature.fox.coffee_counter.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.feature.fox.coffee_counter.data.local.Funding
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

      val user =
          User("98432kljfaf-34980fklaf", name = "Foo", isAdmin = false, password = "324987")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: UserDatabase
    private lateinit var dao: UserDao

    @Before
    fun setUp() {
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
    fun insertUser() = runTest {
        dao.insertUser(user)

        val allUsers = dao.observeAllUsers().getOrAwaitValue()

        assertThat(allUsers.contains(user))
    }

    @Test
    fun insertFunding() = runTest {
        dao.insertUser(user)
        val funding = Funding(123456789, user.id, 20.0)
        dao.insertFunding(listOf(funding))
        val fundingAndUser = dao.getFundingsOfUser(user.id)

        assertThat(fundingAndUser[0].fundings[0]).isEqualTo(funding)
        assertThat(fundingAndUser[0].fundings[0].userId).isEqualTo(user.id)
    }

    @Test
    fun deleteUser() = runTest {
        dao.insertUser(user)
        dao.deleteUser(user)

        val allUsers = dao.observeAllUsers().getOrAwaitValue()


        assertThat(allUsers).doesNotContain(user)
    }

    @Test
    fun getUserById() = runTest {
        dao.insertUser(user)

        val userById = dao.getUserById(user.id)

        assertThat(userById).isEqualTo(user)
    }

    @Test
    fun testSuccessfulLogin() = runTest {

    dao.insertUser(user)

        assertThat(dao.login(user.id, user.password)).isEqualTo(true)


    }

    @Test
    fun testWrongPassword() = runTest {

        dao.insertUser(user)

        assertThat(dao.login(user.id, "")).isEqualTo(false)

    }

    @Test
    fun testWrongUserId() = runTest {

        dao.insertUser(user)

        assertThat(dao.login("", user.password)).isEqualTo(false)
    }
}
