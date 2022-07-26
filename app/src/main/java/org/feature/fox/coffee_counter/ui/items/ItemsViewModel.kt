package org.feature.fox.coffee_counter.ui.items


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.local.database.tables.Item
import org.feature.fox.coffee_counter.data.models.body.ItemBody
import org.feature.fox.coffee_counter.data.models.body.PurchaseBody
import org.feature.fox.coffee_counter.data.repository.ItemRepository
import org.feature.fox.coffee_counter.data.repository.UserRepository
import org.feature.fox.coffee_counter.di.services.AppPreference
import org.feature.fox.coffee_counter.util.IToast
import org.feature.fox.coffee_counter.util.UIText
import javax.inject.Inject

interface IItemsViewModel : IToast {
    var availableItemsState: SnapshotStateList<Item>
    val itemsInShoppingCartState: SnapshotStateList<Item>
    val currentShoppingCartAmountState: MutableState<Double>
    val adminView: MutableState<Boolean>
    val isAdmin: MutableState<Boolean>
    var originalItemId: MutableState<String>
    var currentItemId: MutableState<TextFieldValue>
    var currentItemName: MutableState<TextFieldValue>
    var currentItemAmount: MutableState<TextFieldValue>
    var currentItemPrice: MutableState<TextFieldValue>
    val isLoaded: MutableState<Boolean>
    val addItemDialogVisible: MutableState<Boolean>
    val editItemDialogVisible: MutableState<Boolean>
    val confirmBuyItemDialogVisible: MutableState<Boolean>
    val balance: MutableState<Double>

    suspend fun getItems()
    suspend fun addItemToShoppingCart(item: Item): Boolean
    suspend fun addStringItemToShoppingCart(item: String)
    suspend fun removeItemFromShoppingCart(item: Item)
    suspend fun getItemCartAmount(item: Item): Int
    suspend fun buyItems()
    suspend fun addItem(): Boolean
    suspend fun updateItem()
    suspend fun deleteItem()
    suspend fun getTotalBalance()
}

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val userRepository: UserRepository,
    private val preference: AppPreference,
) : ViewModel(), IItemsViewModel {
    override var availableItemsState = mutableStateListOf<Item>()
    override var itemsInShoppingCartState = mutableStateListOf<Item>()
    override val currentShoppingCartAmountState = mutableStateOf(0.0)
    override val toastChannel = Channel<UIText>()
    override val toast = toastChannel.receiveAsFlow()
    override val adminView = mutableStateOf(false)
    override val isAdmin = mutableStateOf(true)
    override var originalItemId = mutableStateOf(String())
    override var currentItemId = mutableStateOf(TextFieldValue())
    override var currentItemName = mutableStateOf(TextFieldValue())
    override var currentItemAmount = mutableStateOf(TextFieldValue())
    override var currentItemPrice = mutableStateOf(TextFieldValue())
    override val isLoaded = mutableStateOf(false)
    override val addItemDialogVisible = mutableStateOf(false)
    override val editItemDialogVisible = mutableStateOf(false)
    override val confirmBuyItemDialogVisible = mutableStateOf(false)
    override val balance = mutableStateOf(0.0)

    init {
        viewModelScope.launch {
            getItems()
            getTotalBalance()
        }
    }

    override suspend fun getItems() {
        val response = itemRepository.getItems()

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }

        availableItemsState = mutableStateListOf()
        response.data.forEach { item ->
            availableItemsState.add(
                Item(
                    id = item.id,
                    name = item.name,
                    amount = item.amount,
                    price = item.price,
                )
            )
        }

        if (itemsInShoppingCartState.isEmpty()) {
            itemsInShoppingCartState = mutableStateListOf()
            availableItemsState.forEach { item ->
                itemsInShoppingCartState.add(
                    Item(
                        id = item.id,
                        name = item.name,
                        amount = 0,
                        price = item.price,
                    )
                )
            }
        }
        isLoaded.value = true
    }

    override suspend fun addItemToShoppingCart(item: Item): Boolean {

        if (item.amount <= 0) {
            return false
        }

        val userResponse = userRepository.getUserById(
            preference.getTag(BuildConfig.USER_ID)
        )

        if (userResponse.data == null) {
            toastChannel.send(userResponse.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return false
        }

        val cartItem: Item = itemsInShoppingCartState.first{it.id == item.id}

        if(cartItem.amount >= item.amount){
            toastChannel.send(UIText.StringResource(R.string.not_available))
            return false
        }
        if (userResponse.data.balance < currentShoppingCartAmountState.value + item.price) {
            toastChannel.send(UIText.StringResource(R.string.not_enough_funding))
            return false
        }

        cartItem.amount += 1
        itemsInShoppingCartState.remove(cartItem)
        itemsInShoppingCartState.add(cartItem)
        currentShoppingCartAmountState.value += item.price
        return true
    }

    override suspend fun addStringItemToShoppingCart(item: String) {
        try {
            val avItem: Item = availableItemsState.first{it.name == item}

            val success = addItemToShoppingCart(avItem)
            if (success){
                confirmBuyItemDialogVisible.value = true
            }

        }catch(e: NoSuchElementException){
            toastChannel.send(UIText.StringResource(R.string.not_exist))
            return
        }
    }

    override suspend fun getItemCartAmount(item: Item): Int {
        val cartItem: Item = itemsInShoppingCartState.first{it.id == item.id}
        return cartItem.amount
    }

    override suspend fun removeItemFromShoppingCart(item: Item) {
        val cartItem: Item = itemsInShoppingCartState.first{it.id == item.id}

        if(cartItem.amount > 0){
            cartItem.amount -= 1
            itemsInShoppingCartState.remove(cartItem)
            itemsInShoppingCartState.add(cartItem)
            currentShoppingCartAmountState.value -= item.price
        }
    }

    override suspend fun buyItems() {
        itemsInShoppingCartState.forEach { cartItem ->
            if (cartItem.amount > 0) {
                val response = itemRepository.purchaseItem(
                    preference.getTag(BuildConfig.USER_ID),
                    PurchaseBody(cartItem.id, cartItem.amount)
                )

                if (response.data == null) {
                    toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                        ?: UIText.StringResource(R.string.unknown_error))
                    return
                }

                currentShoppingCartAmountState.value -= cartItem.price * cartItem.amount
                cartItem.amount = 0
            }
        }
        isLoaded.value = false
        getItems()
        getTotalBalance()
    }

    override suspend fun addItem(): Boolean {
        if (currentItemName.value.text.isEmpty() ||
            currentItemAmount.value.text.isEmpty() ||
            currentItemPrice.value.text.isEmpty()
        ) {
            toastChannel.send(UIText.StringResource(R.string.fill_all_fields))
            return false
        }

        val response = itemRepository.postItem(
            ItemBody(
                name = currentItemName.value.text,
                amount = currentItemAmount.value.text.toInt(),
                price = currentItemPrice.value.text.toDouble(),
            )
        )

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return false
        }
        toastChannel.send(UIText.StringResource(R.string.add_item))
        isLoaded.value = false
        getItems()
        return true
    }

    override suspend fun updateItem() {
        if (currentItemPrice.value.text.toDouble() < 0) {
            toastChannel.send(UIText.StringResource(R.string.price_negative))
            return
        }

        val response = itemRepository.updateItem(
            itemId = originalItemId.value,
            itemBody = ItemBody(
                id = currentItemId.value.text,
                name = currentItemName.value.text,
                amount = currentItemAmount.value.text.toInt(),
                price = currentItemPrice.value.text.toDouble(),
            )
        )

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }
        toastChannel.send(UIText.StringResource(R.string.update_item))
        isLoaded.value = false
        getItems()
    }

    override suspend fun deleteItem() {
        val response = itemRepository.deleteItemById(originalItemId.value)

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }
        toastChannel.send(UIText.StringResource(R.string.delete_item))
        isLoaded.value = false
        getItems()
    }

    override suspend fun getTotalBalance() {
        val response = userRepository.getUserById(preference.getTag(BuildConfig.USER_ID))

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }
        balance.value = response.data.balance
    }
}

class ItemsViewModelPreview : IItemsViewModel {
    override val currentShoppingCartAmountState = mutableStateOf(55.0)
    override var availableItemsState = mutableStateListOf<Item>()
    override var itemsInShoppingCartState = mutableStateListOf<Item>()
    override val toastChannel = Channel<UIText>()
    override val toast = toastChannel.receiveAsFlow()
    override val adminView = mutableStateOf(false)
    override val isAdmin = mutableStateOf(true)
    override var originalItemId = mutableStateOf(String())
    override var currentItemId = mutableStateOf(TextFieldValue())
    override var currentItemName = mutableStateOf(TextFieldValue())
    override var currentItemAmount = mutableStateOf(TextFieldValue())
    override var currentItemPrice = mutableStateOf(TextFieldValue())
    override val isLoaded = mutableStateOf(false)
    override val addItemDialogVisible = mutableStateOf(false)
    override val editItemDialogVisible = mutableStateOf(false)
    override val confirmBuyItemDialogVisible = mutableStateOf(false)
    override val balance = mutableStateOf(50.0)

    init {
        availableItemsState = mutableStateListOf(
            Item(id = "a", name = "coffee", amount = 69, price = 5.0),
            Item(id = "b", name = "beer", amount = 42, price = 4.99),
            Item(id = "c", name = "mate", amount = 1337, price = 9.99)
        )
        itemsInShoppingCartState = mutableStateListOf(
            Item(id = "a", name = "coffee", amount = 2, price = 5.0),
            Item(id = "b", name = "beer", amount = 5, price = 4.99),
        )
    }

    override suspend fun getItems() {
        TODO("Not yet implemented")
    }

    override suspend fun addItemToShoppingCart(item: Item): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun addStringItemToShoppingCart(item: String) {
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

    override suspend fun addItem(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateItem() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteItem() {
        TODO("Not yet implemented")
    }

    override suspend fun getTotalBalance() {
        TODO("Not yet implemented")
    }
}
