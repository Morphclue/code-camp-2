package org.feature.fox.coffee_counter.ui.items

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.local.database.tables.Item
import org.feature.fox.coffee_counter.ui.common.MoneyAppBar
import org.feature.fox.coffee_counter.ui.common.SearchBar
import org.feature.fox.coffee_counter.ui.theme.CrayolaCopper

@Preview(showSystemUi = true)
@Composable
fun ItemsViewPreview() {
    ItemsView(ItemsViewModelPreview())
}


@Composable
fun ItemsView(
    itemViewModel: IItemsViewModel,
) {
    Scaffold(
        topBar = { } ,
        floatingActionButton = {
            if(itemViewModel.isAdmin.value) EditFAB(itemViewModel)
            if(itemViewModel.adminView.value) AddItemFAB(itemViewModel)
            if(!itemViewModel.adminView.value) BuyFAB(itemViewModel)
        },
        content = {
            Column {
                MoneyAppBar(title = stringResource(R.string.item_list_title))
                SearchBar()
                ItemList(itemViewModel)
//                if(!itemViewModel.adminView.value!!) BuyButton(itemViewModel)
            }
        }

    )
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
//                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            coroutineScope.launch {
                viewModel.getItems()
            }
            if(viewModel.adminView.value!!) AmountTitle()
            viewModel.availableItemsState.value?.forEach { item ->
                if(viewModel.adminView.value!!){
                    AdminItemRow(viewModel, item)
                }else {
                    ItemRow(viewModel, item)
                }
                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 1.dp
                )
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ItemRow(viewModel: IItemsViewModel, item: Item) {
    val coroutineScope = rememberCoroutineScope()
    var buyItems by remember { mutableStateOf(0) }

    coroutineScope.launch {
        buyItems = viewModel.getItemCartAmount(item)
    }
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
                    "$buyItems/${item.amount}",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.width(60.dp)
                )

                // TODO: move in own function
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.addItemToShoppingCart(item)
                            buyItems = viewModel.getItemCartAmount(item)
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

                // TODO: move in own function
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.removeItemFromShoppingCart(item)
                            buyItems = viewModel.getItemCartAmount(item)
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
fun AdminItemRow(viewModel: IItemsViewModel, item: Item){
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
                    "${item.amount}",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.width(60.dp)
                )
            }
        }
    }
}

@Composable
fun AddButton(viewModel: IItemsViewModel, item: Item){
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

@Composable
fun AmountTitle(){
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
fun EditFAB(viewModel: IItemsViewModel){
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
fun AddItemFAB(viewModel: IItemsViewModel){
    ExtendedFloatingActionButton(
        modifier = Modifier.padding(bottom = 50.dp),
        backgroundColor = CrayolaCopper,
        text = {
            Text("Add Item")
        },
        onClick = {}
    )
}

@Composable
fun BuyFAB(viewModel: IItemsViewModel){
    val coroutineScope = rememberCoroutineScope()
    ExtendedFloatingActionButton(
        modifier = Modifier.padding(bottom = 50.dp),
        backgroundColor = CrayolaCopper,
        text = {
            Text("Buy (${String.format("%.2f", viewModel.currentShoppingCartAmountState.value)}€)")
        },
        onClick = {
            coroutineScope.launch {
                viewModel.buyItems()
            }
        }
    )
}


@Composable
fun BuyButton(viewModel: IItemsViewModel) {
    val coroutineScope = rememberCoroutineScope()
    Button(
        onClick = {
            coroutineScope.launch {
                viewModel.buyItems()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = CrayolaCopper,
            contentColor = Color.White
        )
    ) {
        Text(
            "Buy (${String.format("%.2f", viewModel.currentShoppingCartAmountState.value)}€)",
            fontSize = 20.sp,
        )
    }
}
