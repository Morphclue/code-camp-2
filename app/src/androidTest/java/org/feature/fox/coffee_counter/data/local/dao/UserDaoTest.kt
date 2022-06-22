package org.feature.fox.coffee_counter.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.feature.fox.coffee_counter.data.local.UserDatabase
import org.feature.fox.coffee_counter.data.local.database.dao.UserDao
import org.feature.fox.coffee_counter.data.local.database.tables.Funding
import org.feature.fox.coffee_counter.data.local.database.tables.Purchase
import org.feature.fox.coffee_counter.data.local.database.tables.User
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

    private val user1 =
        User("98432kljfaf-34980fklaf", name = "Foo", isAdmin = false, password = "324987")
    private val user2 =
        User("34980fklaf-98432kljfaf", name = "Bar", isAdmin = false, password = "1234")
    private val funding1 = Funding(123456789, user1.id, 20.0)
    private val funding2 = Funding(123456799, user1.id, 10.0)
    private val funding3 = Funding(123456999, user2.id, 5.0)
    private val purchase1 = Purchase(111111111, user1.id, -3.5, "item-1", "coffee", 2)
    private val purchase2 = Purchase(222222222, user1.id, -20.0, "item-2", "beer", 5)
    private val purchase3 = Purchase(333333333, user2.id, -8.0, "item-2", "beer", 2)

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
    fun testInsertUser() = runTest {
        dao.insertUser(user1)

        val allUsers = dao.observeAllUsers().getOrAwaitValue()

        assertThat(allUsers.contains(user1))
    }

    @Test
    fun testInsertFunding() = runTest {
        dao.insertUser(user1)
        dao.insertFunding(funding1)
        dao.insertFunding(funding2)
        val fundingList = dao.getFundingListOfUser(user1.id)

        assertThat(fundingList.size).isEqualTo(2)
        assertThat(fundingList[0]).isEqualTo(funding1)
        assertThat(fundingList[1]).isEqualTo(funding2)

        assertThat(fundingList[0].userId).isEqualTo(user1.id)
        assertThat(fundingList[1].userId).isEqualTo(user1.id)

    }

    @Test
    fun testInsertPurchase() = runTest {
        dao.insertUser(user1)
        dao.insertPurchase(purchase1)
        dao.insertPurchase(purchase2)
        val purchaseList = dao.getPurchaseListOfUser(user1.id)

        assertThat(purchaseList.size).isEqualTo(2)
        assertThat(purchaseList[0]).isEqualTo(purchase1)
        assertThat(purchaseList[1]).isEqualTo(purchase2)

        assertThat(purchaseList[0].userId).isEqualTo(user1.id)
        assertThat(purchaseList[1].userId).isEqualTo(user1.id)
    }

    @Test
    fun testInsertAndDeleteFunding() = runTest {
        dao.insertUser(user1)
        dao.insertFunding(funding1)
        dao.insertFunding(funding2)
        var fundingList = dao.getFundingListOfUser(user1.id)

        assertThat(fundingList.size).isEqualTo(2)

        dao.deleteFunding(funding1)

        fundingList = dao.getFundingListOfUser(user1.id)

        assertThat(fundingList.size).isEqualTo(1)
        assertThat(fundingList[0]).isEqualTo(funding2)

    }

    @Test
    fun testInsertAndDeletePurchase() = runTest {
        dao.insertUser(user1)
        dao.insertPurchase(purchase1)
        dao.insertPurchase(purchase2)
        var purchaseList = dao.getPurchaseListOfUser(user1.id)

        assertThat(purchaseList.size).isEqualTo(2)

        dao.deletePurchase(purchase1)

        purchaseList = dao.getPurchaseListOfUser(user1.id)

        assertThat(purchaseList.size).isEqualTo(1)
        assertThat(purchaseList[0]).isEqualTo(purchase2)

    }

    @Test
    fun testDeleteUser() = runTest {
        dao.insertUser(user1)
        dao.deleteUser(user1)

        val allUsers = dao.observeAllUsers().getOrAwaitValue()


        assertThat(allUsers).doesNotContain(user1)
    }

    @Test
    fun testFundingAndPurchaseForeignKeyOnDelete() = runTest {
        dao.insertUser(user1)
        dao.insertUser(user2)
        dao.insertFunding(funding1)
        dao.insertFunding(funding2)
        dao.insertFunding(funding3)
        dao.insertPurchase(purchase1)
        dao.insertPurchase(purchase2)
        dao.insertPurchase(purchase3)

        var fundingList = dao.getFundingListOfUser(user1.id)
        var purchaseList = dao.getPurchaseListOfUser(user1.id)

        assertThat(fundingList.size).isEqualTo(2)
        assertThat(purchaseList.size).isEqualTo(2)

        dao.deleteUser(user1)

        fundingList = dao.getFundingListOfUser(user1.id)
        purchaseList = dao.getPurchaseListOfUser(user1.id)

        assertThat(fundingList).isEmpty()
        assertThat(purchaseList).isEmpty()

        fundingList = dao.getFundingListOfUser(user2.id)
        purchaseList = dao.getPurchaseListOfUser(user2.id)

        assertThat(fundingList.size).isEqualTo(1)
        assertThat(purchaseList.size).isEqualTo(1)

        assertThat(fundingList[0]).isEqualTo(funding3)
        assertThat(purchaseList[0]).isEqualTo(purchase3)

        assertThat(fundingList[0].userId).isEqualTo(user2.id)
        assertThat(purchaseList[0].userId).isEqualTo(user2.id)
    }

    @Test
    fun testObserveTotalBalance() = runTest {
        dao.insertUser(user1)
        dao.insertFunding(funding1)
        dao.insertFunding(funding2)
        dao.insertPurchase(purchase1)
        dao.insertPurchase(purchase2)

        val totalBalance = dao.observeTotalBalanceOfUser(user1.id).getOrAwaitValue()

        assertThat(totalBalance).isEqualTo(funding1.value + funding2.value + purchase1.totalValue + purchase2.totalValue)
    }

    @Test
    fun testGetUserById() = runTest {
        dao.insertUser(user1)

        val userById = dao.getUserById(user1.id)

        assertThat(userById).isEqualTo(user1)
    }

    @Test
    fun testSuccessfulLogin() = runTest {

        dao.insertUser(user1)

        assertThat(dao.login(user1.id, user1.password)).isEqualTo(true)


    }

    @Test
    fun testWrongPassword() = runTest {

        dao.insertUser(user1)

        assertThat(dao.login(user1.id, "")).isEqualTo(false)

    }

    @Test
    fun testWrongUserId() = runTest {

        dao.insertUser(user1)

        assertThat(dao.login("", user1.password)).isEqualTo(false)
    }
}
