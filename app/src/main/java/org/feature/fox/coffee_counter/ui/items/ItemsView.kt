package org.feature.fox.coffee_counter.ui.items

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.local.database.tables.Item
import org.feature.fox.coffee_counter.ui.common.LoadingAnimation
import org.feature.fox.coffee_counter.ui.common.MoneyAppBar
import org.feature.fox.coffee_counter.ui.common.SearchBar
import org.feature.fox.coffee_counter.ui.common.ToastMessage
import org.feature.fox.coffee_counter.ui.theme.CrayolaCopper

/**
 * Preview for the ItemsView.
 */
@Preview(showSystemUi = true)
@Composable
fun ItemsViewPreview() {
    ItemsView(ItemsViewModelPreview())
}

/**
 * Main composable for the ItemsView.
 * @param viewModel the ItemList ViewModel.
 */
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ItemsView(
    viewModel: IItemsViewModel,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    ToastMessage(viewModel, context)
    AddItemDialog(viewModel)
    EditItemDialog(viewModel)
    ConfirmBuyDialog(viewModel)

    Scaffold(
        topBar = {
            MoneyAppBar(
                pair = Pair(
                    stringResource(id = R.string.item_list_title),
                    viewModel.balance
                )
            )
        },
        floatingActionButton = {
            if (viewModel.adminView.value) AddItemFAB(viewModel)
            if (!viewModel.adminView.value) BuyFAB(viewModel)
        },
        floatingActionButtonPosition = FabPosition.Center,
    ){
        Column {
            coroutineScope.launch {
                viewModel.getTotalBalance()
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            )
            {
                SearchBar(
                    fraction = if (viewModel.isAdmin.value) 0.8f else 1f,
                    state = viewModel.searchField,
                    onValueChanged = {
                        viewModel.search()
                    },
                )
                if (viewModel.isAdmin.value) SwitchAdminView(viewModel)
            }
            if (viewModel.isLoaded.value) ItemList(viewModel) else LoadingBox()
        }
    }
}

/**
 * Loading box for when the item list is loading.
 */
@Composable
fun LoadingBox() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoadingAnimation()
    }
}

/**
 * Composable for the item list.
 * @param viewModel the ItemsList ViewModel.
 */
@Composable
fun ItemList(viewModel: IItemsViewModel) {
    Column {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(5.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val recommendedItem = viewModel.recommendedItem.value
            if (recommendedItem != null && !viewModel.adminView.value) {
                Text(
                    text = stringResource(id = R.string.recommendation),
                )
                ItemRow(viewModel, recommendedItem)
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp
                )
            }

            if (viewModel.adminView.value) AmountTitle()
            viewModel.filteredItemsList.forEach { item ->
                if (viewModel.adminView.value) {
                    AdminItemRow(viewModel, item)
                } else {
                    ItemRow(viewModel, item)
                }
            }
            Box(Modifier.height(90.dp))
        }
    }
}

/**
 * A single Item row in the normal view.
 * @param viewModel the ItemsList ViewModel.
 * @param item the Item to be displayed.
 */
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ItemRow(viewModel: IItemsViewModel, item: Item) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = 5.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Column {
                Text(item.name, fontWeight = FontWeight.Medium)
                Text("${String.format("%.2f", item.price)}€", color = Color.Gray)
            }

            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${viewModel.itemsInShoppingCartState.first { it.id == item.id }.amount}" +
                                "/${item.amount}",
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.width(70.dp)
                    )

                    AddToCartButton(viewModel, item)
                    RemoveFromCartButton(viewModel, item)
                }
            }
        }
    }
}

/**
 * A button that allows to add an Item to the shopping cart.
 * @param viewModel the ItemsList ViewModel.
 * @param item the Item to be added.
 */
@Composable
fun AddToCartButton(viewModel: IItemsViewModel, item: Item) {
    val coroutineScope = rememberCoroutineScope()
    Button(
        onClick = {
            coroutineScope.launch {
                viewModel.addItemToShoppingCart(item)
            }
        },
        modifier = Modifier.size(35.dp),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = "content description",
            tint = Color.White
        )
    }
}

/**
 * A button that allows to delete an Item from the shopping cart.
 * @param viewModel the ItemsList ViewModel.
 * @param item the Item to be removed.
 */
@Composable
fun RemoveFromCartButton(viewModel: IItemsViewModel, item: Item) {
    val coroutineScope = rememberCoroutineScope()
    Button(
        onClick = {
            coroutineScope.launch {
                viewModel.removeItemFromShoppingCart(item)
            }
        },
        modifier = Modifier.size(35.dp),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
    ) {
        Icon(
            Icons.Default.Remove,
            contentDescription = "content description",
            tint = Color.White
        )
    }
}


/**
 * A single Item row in the admin view.
 * @param viewModel the ItemsList ViewModel.
 * @param item the Item to be displayed.
 */
@Composable
fun AdminItemRow(viewModel: IItemsViewModel, item: Item) {
    val scope = rememberCoroutineScope()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = 5.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .clickable {
                    viewModel.originalItemId.value = TextFieldValue(item.id).text
                    viewModel.currentItemId.value = TextFieldValue(item.id)
                    viewModel.currentItemName.value = TextFieldValue(item.name)
                    viewModel.currentItemAmount.value = TextFieldValue(item.amount.toString())
                    viewModel.currentItemPrice.value = TextFieldValue(item.price.toString())
                    scope.launch {
                        viewModel.editItemDialogVisible.value = true
                    }
                }
        ) {
            Column {
                Text(item.name, fontWeight = FontWeight.Medium)
                Text("${String.format("%.2f", item.price)}€", color = Color.Gray)
            }
            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${item.amount}",
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.width(60.dp)
                    )
                }
            }
        }
    }
}

/**
 * Composable for amount column in the admin view.
 */
@Composable
fun AmountTitle() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                "Amount",
                fontWeight = FontWeight.Medium,
                color = CrayolaCopper,
                modifier = Modifier.width(60.dp)
            )
        }
    }
}

/**
 * A button that allows to switch to the admin view and back to the normal view.
 * @param viewModel the ItemsList ViewModel.
 */
@Composable
fun SwitchAdminView(viewModel: IItemsViewModel) {
    Button(
        shape = CircleShape,
        onClick = {
            viewModel.adminView.value = !viewModel.adminView.value
        })
    {

        Icon(
            Icons.Outlined.Sync,
            contentDescription = stringResource(R.string.switch_admin_view),
        )
    }
}

/**
 * A button that allows to create an Item and add it to the Database.
 * @param viewModel the ItemsList ViewModel.
 */
@Composable
fun AddItemFAB(viewModel: IItemsViewModel) {
    ExtendedFloatingActionButton(
        modifier = Modifier.padding(bottom = 50.dp),
        backgroundColor = CrayolaCopper,
        text = {
            Text("Add Item")
        },
        onClick = { viewModel.addItemDialogVisible.value = true }
    )
}

/**
 * A button that allows to buy everything currently in the shopping cart.
 * @param viewModel the ItemsList ViewModel.
 */
@Composable
fun BuyFAB(viewModel: IItemsViewModel) {
    val coroutineScope = rememberCoroutineScope()
    ExtendedFloatingActionButton(
        modifier = Modifier.padding(bottom = 50.dp),
        backgroundColor = CrayolaCopper,
        text = {
            Text(
                "Buy (${
                    String.format(
                        "%.2f",
                        viewModel.currentShoppingCartAmountState.value
                    )
                }€)"
            )
        },
        onClick = {
            coroutineScope.launch {
                viewModel.confirmBuyItemDialogVisible.value = true
            }
        }
    )
}
