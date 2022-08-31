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
import org.feature.fox.coffee_counter.data.local.database.tables.Purchase
import org.feature.fox.coffee_counter.data.models.body.ItemBody
import org.feature.fox.coffee_counter.data.models.body.PurchaseBody
import org.feature.fox.coffee_counter.data.repository.ItemRepository
import org.feature.fox.coffee_counter.data.repository.UserRepository
import org.feature.fox.coffee_counter.di.services.AchievementGeneration
import org.feature.fox.coffee_counter.di.services.AppPreference
import org.feature.fox.coffee_counter.util.IToast
import org.feature.fox.coffee_counter.util.UIText
import org.feature.fox.coffee_counter.util.Utils
import javax.inject.Inject

interface IItemsViewModel : IToast {
    var availableItemsList: SnapshotStateList<Item>
    var filteredItemsList: SnapshotStateList<Item>
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
    val searchField: MutableState<TextFieldValue>
    val recommendedItem: MutableState<Item?>

    suspend fun getItems()
    suspend fun addItemToShoppingCart(item: Item): Boolean
    suspend fun addStringItemToShoppingCart(item: String)
    suspend fun removeItemFromShoppingCart(item: Item)
    suspend fun buyItems()
    suspend fun addItem(): Boolean
    suspend fun updateItem()
    suspend fun deleteItem()
    suspend fun getTotalBalance()
    suspend fun generateRecommendation()
    fun search()
}

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val userRepository: UserRepository,
    private val preference: AppPreference,
    private val achievementGenerator: AchievementGeneration,
) : ViewModel(), IItemsViewModel {
    override var availableItemsList = mutableStateListOf<Item>()
    override var filteredItemsList = mutableStateListOf<Item>()
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
    override val searchField = mutableStateOf(TextFieldValue())
    override val recommendedItem = mutableStateOf<Item?>(null)

    init {
        viewModelScope.launch {
            isAdmin.value = preference.getTag(BuildConfig.IS_ADMIN, true)
            getItems()
            generateRecommendation()
        }
    }

    /**
     *  Loads the item list
     */
    override suspend fun getItems() {
        val response = itemRepository.getItems()

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }

        availableItemsList = mutableStateListOf()
        response.data.forEach { item ->

            // insert/update Items into db

            itemRepository.insertItemDb(
                Item(
                    id = item.id,
                    name = item.name,
                    amount = item.amount,
                    price = item.price
                )
            )
            availableItemsList.add(
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
            availableItemsList.forEach { item ->
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
        filteredItemsList = mutableStateListOf()
        filteredItemsList.addAll(availableItemsList)
        isLoaded.value = true
    }

    /**
     *  Adds the item to the shopping cart
     *  @param item The item
     */
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

        val cartItem: Item = itemsInShoppingCartState.first { it.id == item.id }

        if (cartItem.amount >= item.amount) {
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

    /**
     *  Adds the item with the given id to the shopping cart
     *  @param item The id of the item
     */
    override suspend fun addStringItemToShoppingCart(item: String) {
        try {
            val avItem: Item = availableItemsList.first { it.id == item }

            val success = addItemToShoppingCart(avItem)
            if (success) {
                confirmBuyItemDialogVisible.value = true
            }

        } catch (e: NoSuchElementException) {
            toastChannel.send(UIText.StringResource(R.string.not_exist))
            return
        }
    }

    /**
     *  Removes item from the shopping cart
     *  @param item The item
     */
    override suspend fun removeItemFromShoppingCart(item: Item) {
        val cartItem: Item = itemsInShoppingCartState.first { it.id == item.id }

        if (cartItem.amount > 0) {
            cartItem.amount -= 1
            itemsInShoppingCartState.remove(cartItem)
            itemsInShoppingCartState.add(cartItem)
            currentShoppingCartAmountState.value -= item.price
        }
    }

    /**
     *  Buys the items in the shopping cart
     */
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

                // Fetch Transactions from API and get last transaction (the purchase above) to get right timestamp

                val transactionResponse =
                    userRepository.getTransactions(preference.getTag(BuildConfig.USER_ID))
                if (transactionResponse.data == null) {
                    toastChannel.send(transactionResponse.message?.let { UIText.DynamicString(it) }
                        ?: UIText.StringResource(R.string.unknown_error))
                    return
                }

                userRepository.insertPurchaseDb(
                    Purchase(
                        timestamp = transactionResponse.data.last().timestamp,
                        userId = preference.getTag(BuildConfig.USER_ID),
                        totalValue = transactionResponse.data.last().value,
                        itemName = transactionResponse.data.last().itemName!!,
                        itemId = transactionResponse.data.last().itemId!!,
                        amount = transactionResponse.data.last().amount!!,
                    )
                )
                currentShoppingCartAmountState.value -= cartItem.price * cartItem.amount
                cartItem.amount = 0
            }
        }
        searchField.value = TextFieldValue("")
        achievementGenerator.checkAchievements(availableItemsList)
        isLoaded.value = false
        getItems()
        generateRecommendation()
    }

    /**
     *  Adds a new Item with the given values.
     */
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

        // add new added item to DB
        itemRepository.insertItemDb(
            Item(
                id = response.data,
                name = currentItemName.value.text,
                amount = currentItemAmount.value.text.toInt(),
                price = currentItemPrice.value.text.toDouble()
            )
        )
        toastChannel.send(UIText.StringResource(R.string.add_item_success))
        isLoaded.value = false
        getItems()
        return true
    }

    /**
     *  Updates the selected Item
     */
    override suspend fun updateItem() {
        if (currentItemPrice.value.text.toDouble() < 0) {
            toastChannel.send(UIText.StringResource(R.string.price_negative))
            return
        }

        val response = itemRepository.updateItem(
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

        // Update Item in DB
        itemRepository.updateItemDb(
            Item(
                id = currentItemId.value.text,
                name = currentItemName.value.text,
                price = currentItemPrice.value.text.toDouble(),
                amount = currentItemAmount.value.text.toInt(),
            )
        )

        toastChannel.send(UIText.StringResource(R.string.update_item))
        isLoaded.value = false
        getItems()
    }

    /**
     * Deletes the selected item.
     */
    override suspend fun deleteItem() {
        val itemToBeDeleted = itemRepository.getItemById(originalItemId.value)
        val response = itemRepository.deleteItemById(originalItemId.value)

        if (response.data == null || itemToBeDeleted.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }

        // Delete Item From DB
        itemRepository.deleteItemDb(
            Item(
                id = itemToBeDeleted.data.id,
                name = itemToBeDeleted.data.name,
                amount = itemToBeDeleted.data.amount,
                price = itemToBeDeleted.data.price

            )
        )

        toastChannel.send(UIText.StringResource(R.string.delete_item))
        isLoaded.value = false
        getItems()
    }

    /**
     * Gets the total balance of the user.
     * FIXME: Maybe use "observeTotalBalance" instead of calling this Method after each change
     */
    override suspend fun getTotalBalance() {
        val response = userRepository.getUserById(preference.getTag(BuildConfig.USER_ID))

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }
        balance.value = response.data.balance
    }

    override suspend fun generateRecommendation() {
        val purchases: List<Purchase> =
            userRepository.getPurchaseListOfUserDb(preference.getTag(BuildConfig.USER_ID))

        if (purchases.isEmpty()) {
            return
        }

        val threeHours = 10800000
        val twentyFourHours = 86400000
        val currentTime = System.currentTimeMillis() % twentyFourHours
        val minTime = currentTime - threeHours
        val maxTime = currentTime + threeHours

        val filteredPurchases = mutableListOf<Purchase>()
        for (purchase in purchases) {
            val moduloTimeStamp = purchase.timestamp % twentyFourHours
            if (minTime < 0) {
                if (moduloTimeStamp in (twentyFourHours - minTime)..twentyFourHours
                    || moduloTimeStamp in 0..maxTime
                ) {
                    filteredPurchases.add(purchase)
                }
            } else if (twentyFourHours < maxTime) {
                if (moduloTimeStamp in 0..(maxTime - twentyFourHours)
                    || moduloTimeStamp in minTime..twentyFourHours
                ) {
                    filteredPurchases.add(purchase)
                }
            } else {
                if (moduloTimeStamp in minTime..maxTime) {
                    filteredPurchases.add(purchase)
                }
            }
        }

        val groupedPurchases =
            filteredPurchases.groupBy { it.itemId }.map { it.key to it.value.size }
        val maxValue = groupedPurchases.maxByOrNull { it.second } ?: return
        if (maxValue.second < 3) {
            return
        }

        try {
            recommendedItem.value = availableItemsList.first { it.id == maxValue.first }
        } catch (e: NoSuchElementException) {
            return
        }
    }

    /**
     * Searches for an item by fuzzy search.
     */
    override fun search() {
        Utils.fuzzySearchItems(filteredItemsList, availableItemsList, searchField.value.text)
    }
}

class ItemsViewModelPreview : IItemsViewModel {
    override val currentShoppingCartAmountState = mutableStateOf(55.0)
    override var availableItemsList = mutableStateListOf<Item>()
    override var filteredItemsList = mutableStateListOf<Item>()
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
    override val searchField = mutableStateOf(TextFieldValue())
    override val recommendedItem = mutableStateOf<Item?>(null)

    init {
        availableItemsList = mutableStateListOf(
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

    override suspend fun generateRecommendation() {
        TODO("Not yet implemented")
    }

    override fun search() {
        TODO("Not yet implemented")
    }
}
