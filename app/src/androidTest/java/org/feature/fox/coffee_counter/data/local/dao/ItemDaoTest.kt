package org.feature.fox.coffee_counter.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.feature.fox.coffee_counter.data.local.Item
import org.feature.fox.coffee_counter.data.local.ItemDatabase
import org.feature.fox.coffee_counter.getOrAwaitValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ItemDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var itemDb: ItemDatabase
    private lateinit var itemDao: ItemDao

    @Before
    fun setUp() {
        itemDb = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ItemDatabase::class.java
        ).allowMainThreadQueries().build()
        itemDao = itemDb.itemDao()
    }

    @After
    fun tearDown() {
        itemDb.close()
    }

    @Test
    fun insertItem() = runTest {
        val item1 = Item("98432kljfaf-34980fklaf", amount = 2, price = 23.20, name = "Cola")
        itemDao.insertItem(item1)

        val item2 = Item(id = "", amount = 3, price = 1.99, name = "Cola")
        itemDao.insertItem(item2)

        val allItems = itemDao.observeAllItems().getOrAwaitValue()

        assertThat(allItems.contains(item1) && allItems.contains(item2))
    }

    @Test
    fun insertItemEmptyAmount() = runTest {
        val item = Item(id = "", price = 1.99, name = "Cola")
        itemDao.insertItem(item)

        val allItems = itemDao.observeAllItems().getOrAwaitValue()

        assertThat(allItems[allItems.indexOf(item)].amount).isEqualTo(0)
    }

    @Test
    fun deleteItem() = runTest {
        val item = Item("98432kljfaf-34980fklaf", amount = 2, price = 23.20, name = "Cola")
        itemDao.insertItem(item)
        itemDao.deleteItem(item)

        val allItems = itemDao.observeAllItems().getOrAwaitValue()

        assertThat(allItems).doesNotContain(item)
    }

    @Test
    fun observeTotalPriceSum() = runTest {
        val item1 = Item("98432kljfaf-34980fkla1", amount = 2, price = 0.99, name = "Cola")
        val item2 = Item("98432kljfaf-34980fkla2", amount = 5, price = 1.50, name = "Kaffee")
        val item3 = Item("98432kljfaf-34980fkla3", amount = 10, price = 20.0, name = "Bier")

        itemDao.insertItem(item1)
        itemDao.insertItem(item2)
        itemDao.insertItem(item3)

        val totalPriceSum = itemDao.observeTotalPrice().getOrAwaitValue()

        assertThat(totalPriceSum).isEqualTo(2 * 0.99 + 5 * 1.50 + 10 * 20.0)
    }

    @Test
    fun getItemById() = runTest {
        val item = Item("98432kljfaf-34980fkla1", amount = 2, price = 0.99, name = "Cola")
        itemDao.insertItem(item)

        val itemById = itemDao.getItemById(item.id)

        assertThat(itemById).isEqualTo(item)
    }
}
