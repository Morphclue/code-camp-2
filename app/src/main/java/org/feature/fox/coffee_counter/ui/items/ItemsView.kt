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
import androidx.compose.material.Divider
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
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

@Preview(showSystemUi = true)
@Composable
fun ItemsViewPreview() {
    ItemsView(ItemsViewModelPreview())
}


@Composable
fun ItemsView(
    viewModel: IItemsViewModel,
) {
    val context = LocalContext.current
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
            if (viewModel.isAdmin.value) EditFAB(viewModel)
            if (viewModel.adminView.value) AddItemFAB(viewModel)
            if (!viewModel.adminView.value) BuyFAB(viewModel)
        },
        content = {
            Column {
                SearchBar()
                if (viewModel.isLoaded.value) ItemList(viewModel) else LoadingBox()
            }
        }
    )
}


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

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ItemList(viewModel: IItemsViewModel) {
    val coroutineScope = rememberCoroutineScope()

    Column {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(5.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            coroutineScope.launch {
                viewModel.getItems()
                viewModel.getTotalBalance()
            }
            if (viewModel.adminView.value) AmountTitle()
            viewModel.availableItemsState.forEach { item ->
                if (viewModel.adminView.value) {
                    AdminItemRow(viewModel, item)
                } else {
                    ItemRow(viewModel, item)
                }
                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 1.dp
                )
            }
            Box(Modifier.height(90.dp))
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ItemRow(viewModel: IItemsViewModel, item: Item) {
    val coroutineScope = rememberCoroutineScope()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
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
                    modifier = Modifier.width(60.dp)
                )

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
        }
    }
}

@Composable
fun AdminItemRow(viewModel: IItemsViewModel, item: Item) {
    val scope = rememberCoroutineScope()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
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

@Composable
fun EditFAB(viewModel: IItemsViewModel) {
    FloatingActionButton(
        modifier = Modifier.padding(start = 170.dp, bottom = 50.dp),
        onClick = {
            viewModel.adminView.value = !viewModel.adminView.value
        },
        backgroundColor = CrayolaCopper,
        contentColor = Color.White

    ) {
        Icon(Icons.Filled.Add, "")
    }
}

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

@Composable
fun BuyFAB(viewModel: IItemsViewModel) {
    val coroutineScope = rememberCoroutineScope()
    ExtendedFloatingActionButton(
        modifier = Modifier.padding(bottom = 50.dp),
        backgroundColor = CrayolaCopper,
        text = {
            Text("Buy (${String.format("%.2f", viewModel.currentShoppingCartAmountState.value)}€)")
        },
        onClick = {
            coroutineScope.launch {
                viewModel.confirmBuyItemDialogVisible.value = true
            }
        }
    )
}
