package org.feature.fox.coffee_counter.ui.items

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    viewModel: IItemsViewModel,
) {
    Column {
        MoneyAppBar(title = stringResource(R.string.item_list_title))
        SearchBar()
        ItemList(viewModel.availableItemsState)
        BuyButton(amount = viewModel.currentShoppingCartAmountState)
    }
}

@Composable
fun ItemList(items: List<Item>) {
    Column {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(5.dp)
                .fillMaxWidth(),
//                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items.forEach { item ->
                ItemRow(item)
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

@Composable
fun ItemRow(item: Item) {
    var buyItems by remember { mutableStateOf(0) }
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

                Button(
                    onClick = { if (buyItems < item.amount) buyItems++ },
                    modifier = Modifier.size(35.dp),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Icon(Icons.Default.Add,
                        contentDescription = "content description",
                        tint = Color.White)
                }

                Button(
                    onClick = { if (buyItems > 0) buyItems-- },
                    modifier = Modifier.size(35.dp),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Icon(Icons.Default.Remove,
                        contentDescription = "content description",
                        tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun BuyButton(amount: MutableState<Double>) {
    Button(onClick = {},
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
            "Buy (${String.format("%.2f", amount.value)}€)",
            fontSize = 20.sp,
        )
    }
}





