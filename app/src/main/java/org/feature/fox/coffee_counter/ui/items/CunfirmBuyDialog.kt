package org.feature.fox.coffee_counter.ui.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ConfirmBuyDialog(
    viewModel: IItemsViewModel,
){
    if (!viewModel.confirmBuyItemDialogVisible.value) {
        return
    }
    Dialog(
        onDismissRequest = { viewModel.confirmBuyItemDialogVisible.value = false },
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Shopping cart",
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = "Do you want to buy the following items:",
                )
                val bullet = "\u2022"
                Row(){
                    viewModel.itemsInShoppingCartState.value?.forEach { cartItem ->
                        if (cartItem.amount > 0) {
                            Column()
                            {
                                Text(text = bullet + "  " + cartItem.name)
                                Text(text = cartItem.amount.toString())
                            }
                        }
                    }
                }
                EditItemDialogButtons(viewModel)
            }
        }
    }
}
