package org.feature.fox.coffee_counter.ui.items


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.R


@Composable
fun ConfirmBuyDialog(
    viewModel: IItemsViewModel,
) {
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
                    text = stringResource(R.string.shopping_cart),
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = stringResource(R.string.confirm_buy),
                )
                val bullet = "\u2022"
                Column {
                    viewModel.itemsInShoppingCartState.forEach { cartItem ->
                        if (cartItem.amount > 0) {
                            Row {
                                Text(
                                    text = "$bullet   ${cartItem.name}",
                                    modifier = Modifier.width(150.dp)
                                )
                                Text(text = cartItem.amount.toString())
                            }
                        }
                    }
                    Divider(
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth(),
                        thickness = 1.dp
                    )

                    Text(
                        text = "${stringResource(R.string.total)} ${
                            String.format(
                                "%.2f",
                                viewModel.currentShoppingCartAmountState.value
                            )
                        }${stringResource(R.string.currency)}"
                    )
                }
                ConfirmBuyDialogButtons(viewModel)
            }
        }
    }
}

@Composable
fun ConfirmBuyDialogButtons(
    viewModel: IItemsViewModel,
) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(onClick = {
            viewModel.confirmBuyItemDialogVisible.value = false
        }) {
            Text(text = stringResource(id = R.string.cancel))
        }
        TextButton(onClick = {
            scope.launch {
                viewModel.buyItems()
                viewModel.confirmBuyItemDialogVisible.value = false
            }
        }) {
            Text(text = stringResource(id = R.string.buy))
        }
    }
}
