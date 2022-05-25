package org.feature.fox.coffee_counter.ui

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.feature.fox.coffee_counter.data.local.Item

@Preview(showSystemUi = true)
@Composable
fun ItemsView() {
    val items = listOf(
        Item(id = "a", name = "coffee", amount = 69, price = 5.0),
        Item(id = "b", name = "beer", amount = 42, price = 4.99),
        Item(id = "c", name = "mate", amount = 1337, price = 9.99))
        ItemList(items)
}

@Composable
fun ItemList(items: List<Item>) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
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

@Composable
fun ItemRow(item: Item){
    var buyItems by remember { mutableStateOf(0) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            Text(item.name, fontWeight = FontWeight.Medium)
            Text("${item.price}€", color = Color.Gray)
        }


        Column{
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text("$buyItems/${item.amount}", fontWeight = FontWeight.Medium)

                Button(onClick = {if (buyItems < item.amount) buyItems++},
                    modifier= Modifier.size(35.dp),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Icon(Icons.Default.Add ,contentDescription = "content description", tint=Color.White)
                }

                Button(onClick = {if (buyItems > 0) buyItems-=1},
                    modifier= Modifier.size(35.dp),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Icon(Icons.Default.Remove ,contentDescription = "content description", tint=Color.White)
                }
            }
        }
    }
}
