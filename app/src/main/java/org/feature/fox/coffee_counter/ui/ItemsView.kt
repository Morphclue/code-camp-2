package org.feature.fox.coffee_counter.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview(showSystemUi = true)
@Composable
fun ItemsView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Items screen")
    }
}



@Composable
fun ItemList(items: List<Item>) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        items.forEach { item ->
            ItemRow(item)
        }
    }
}

@Composable
fun ItemRow(item: Item){
    var buyItems by remember { mutableStateOf(0) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text(item.name)
            Text("${item.price}€")
        }
        Text("$buyItems/${item.amount}")
        Button(onClick = {if (buyItems < item.amount) buyItems++},
            modifier= Modifier.size(40.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
        ) {
            // Adding an Icon "Add" inside the Button
            Icon(Icons.Default.Add ,contentDescription = "content description", tint=Color.White)
        }
        Button(onClick = {if (buyItems > 0) buyItems-=1},
            modifier= Modifier.size(40.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
        ) {
            // Adding an Icon "Add" inside the Button
            Icon(Icons.Default.Remove ,contentDescription = "content description", tint=Color.White)
        }
    }

}
