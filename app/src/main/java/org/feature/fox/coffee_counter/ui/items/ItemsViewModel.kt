package org.feature.fox.coffee_counter.ui.items


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.local.database.tables.Item
import org.feature.fox.coffee_counter.data.models.body.PurchaseBody
import org.feature.fox.coffee_counter.data.repository.ItemRepository
import org.feature.fox.coffee_counter.util.IToast
import org.feature.fox.coffee_counter.util.UIText
import javax.inject.Inject

interface IItemsViewModel : IToast {
    val availableItemsState: MutableLiveData<MutableList<Item>>
    val itemsInShoppingCartState: MutableLiveData<MutableList<Item>>
    val currentShoppingCartAmountState: MutableState<Double>

    suspend fun getItems()
    suspend fun addItemToShoppingCart(item: Item)
    suspend fun removeItemFromShoppingCart(item: Item)
    suspend fun getItemCartAmount(item: Item): Int
    suspend fun buyItems()
    suspend fun addItem()
    suspend fun updateItem()
    suspend fun deleteItem()
}

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
) : ViewModel(), IItemsViewModel {
    override val availableItemsState = MutableLiveData<MutableList<Item>>()
    override val itemsInShoppingCartState = MutableLiveData<MutableList<Item>>()
    override val currentShoppingCartAmountState = mutableStateOf(0.0)
    override val toastChannel = Channel<UIText>()
    override val toast = toastChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            getItems()
        }
    }

    override suspend fun getItems() {
        val response = itemRepository.getItems()

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }

        availableItemsState.value = mutableListOf()

        response.data.forEach { item ->
            availableItemsState.value?.add(
                Item(
                    id = item.id,
                    name = item.name,
                    amount = item.amount,
                    price = item.price,
                )
            )
        }

        if(itemsInShoppingCartState.value.isNullOrEmpty()){
            itemsInShoppingCartState.value = mutableListOf()
            availableItemsState.value?.forEach { item ->
                itemsInShoppingCartState.value?.add(
                    Item(
                        id = item.id,
                        name = item.name,
                        amount = 0,
                        price = item.price,
                    )
                )
            }
        }
    }

    override suspend fun addItemToShoppingCart(item: Item) {

        if (item.amount <= 0) {
            return
        }

        itemsInShoppingCartState.value?.forEach { cartItem ->
            // TODO check for enough funding
            if (item.id == cartItem.id && cartItem.amount < item.amount) {
                cartItem.amount += 1
                currentShoppingCartAmountState.value += item.price
                return
            }
        }
    }

    override suspend fun getItemCartAmount(item: Item): Int {
        itemsInShoppingCartState.value?.forEach { cartItem ->
            if (item.id == cartItem.id) {
                return cartItem.amount
            }
        }
        return 0
    }

    override suspend fun removeItemFromShoppingCart(item: Item) {
        itemsInShoppingCartState.value?.forEach { cartItem ->
            if (item.id == cartItem.id && cartItem.amount > 0) {
                cartItem.amount -= 1
                currentShoppingCartAmountState.value -= item.price
                return
            }
        }
    }

    override suspend fun buyItems() {
        itemsInShoppingCartState.value?.forEach { cartItem ->
            if (cartItem.amount > 0) {
                // TODO validate
                itemRepository.purchaseItem(cartItem.id, PurchaseBody(cartItem.id, cartItem.amount))
            }
        }
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
    override val availableItemsState = MutableLiveData<MutableList<Item>>()
    override val itemsInShoppingCartState = MutableLiveData<MutableList<Item>>()
    override val toastChannel = Channel<UIText>()
    override val toast = toastChannel.receiveAsFlow()

    init {
        availableItemsState.value = mutableListOf(
            Item(id = "a", name = "coffee", amount = 69, price = 5.0),
            Item(id = "b", name = "beer", amount = 42, price = 4.99),
            Item(id = "c", name = "mate", amount = 1337, price = 9.99)
        )
        itemsInShoppingCartState.value = mutableListOf(
            Item(id = "a", name = "coffee", amount = 2, price = 5.0),
            Item(id = "b", name = "beer", amount = 5, price = 4.99),
        )
    }

    override suspend fun getItems() {
        TODO("Not yet implemented")
    }

    override suspend fun addItemToShoppingCart(item: Item) {
        TODO("Not yet implemented")
    }

    override suspend fun removeItemFromShoppingCart(item: Item) {
        TODO("Not yet implemented")
    }

    override suspend fun getItemCartAmount(item: Item): Int {
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
