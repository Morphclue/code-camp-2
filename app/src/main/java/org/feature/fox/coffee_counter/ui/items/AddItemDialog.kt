package org.feature.fox.coffee_counter.ui.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.ui.common.CommonTextField


@Composable
fun AddItemDialog(
    viewModel: IItemsViewModel,
) {
    if (!viewModel.addItemDialogVisible.value) {
        return
    }
    Dialog(
        onDismissRequest = { viewModel.addItemDialogVisible.value = false },
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.add_item),
                    style = MaterialTheme.typography.subtitle1
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(weight = 1f, fill = false)
                        .padding(vertical = 16.dp)
                ) {
                    CommonTextField(
                        state = viewModel.currentItemName,
                        label = stringResource(R.string.name_hint)
                    )
                    CommonTextField(
                        state = viewModel.currentItemPrice,
                        label = stringResource(R.string.price_hint),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    CommonTextField(
                        state = viewModel.currentItemAmount,
                        label = stringResource(R.string.amount),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                AddItemDialogButtons(viewModel)
            }
        }
    }
}

@Composable
fun AddItemDialogButtons(
    viewModel: IItemsViewModel,
) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(onClick = {
            viewModel.addItemDialogVisible.value = false
        }) {
            Text(text = stringResource(id = R.string.cancel))
        }
        TextButton(onClick = {
            scope.launch {
                val success = viewModel.addItem()
                if (success) {
                    viewModel.addItemDialogVisible.value = false
                }
            }
        }) {
            Text(text = stringResource(id = R.string.ok))
        }
    }
}

