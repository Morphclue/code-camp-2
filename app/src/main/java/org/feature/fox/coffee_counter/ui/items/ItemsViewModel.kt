package org.feature.fox.coffee_counter.ui.items


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.feature.fox.coffee_counter.data.local.database.tables.Item
import org.feature.fox.coffee_counter.data.repository.ItemRepository
import javax.inject.Inject

interface IItemsViewModel {
    val availableItemsState = MutableList<Item>
    val itemsInShoppingCartState = MutableList<Item>
    val currentShoppingCartAmountState: MutableState<Double>

    suspend fun getItems()
    suspend fun addItemToShoppingCart()
    suspend fun buyItems()
    suspend fun addItem()
    suspend fun updateItem()
    suspend fun deleteItem()
}

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
) : ViewModel(), IItemsViewModel {
    override val availableItemsState = itemRepository.getItems()
    override val itemsInShoppingCartState = mutableListOf(null)
    override val currentShoppingCartAmountState = mutableStateOf(0.0)

//    init {
//        viewModelScope.launch {
//            val response = userRepository.getUserById(preference.getTag(BuildConfig.USER_ID))
//        }
//    }

    override suspend fun getItems(){

    }
    override suspend fun addItemToShoppingCart() {
        TODO("Not yet implemented")
    }
    override suspend fun buyItems() {
        TODO("Not yet implemented")
    }
    override suspend fun addItem() {
        TODO("Not yet implemented")
    }
    override suspend fun updateItem() {
        TODO("Not yet implemented")
    }
    override suspend fun deleteItem() {
        TODO("Not yet implemented")
    }
}

class ItemsViewModelPreview : IItemsViewModel {
    override val currentShoppingCartAmountState = mutableStateOf(55.0)
    override val availableItemsState = mutableListOf(
        Item(id = "a", name = "coffee", amount = 69, price = 5.0),
        Item(id = "b", name = "beer", amount = 42, price = 4.99),
        Item(id = "c", name = "mate", amount = 1337, price = 9.99)
    )
    override val itemsInShoppingCartState = mutableListOf(
        Item(id = "a", name = "coffee", amount = 2, price = 5.0),
        Item(id = "b", name = "beer", amount = 5, price = 4.99),
    )

    override suspend fun getItems(){
        TODO("Not yet implemented")
    }
    override suspend fun addItemToShoppingCart() {
        TODO("Not yet implemented")
    }
    override suspend fun buyItems() {
        TODO("Not yet implemented")
    }
    override suspend fun addItem() {
        TODO("Not yet implemented")
    }
    override suspend fun updateItem() {
        TODO("Not yet implemented")
    }
    override suspend fun deleteItem() {
        TODO("Not yet implemented")
    }
}
