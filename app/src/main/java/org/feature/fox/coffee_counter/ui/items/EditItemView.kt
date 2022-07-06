@file:OptIn(ExperimentalMaterialApi::class)
package org.feature.fox.coffee_counter.ui.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.ui.common.CustomButton


@Preview(showBackground = true)
@Composable
fun EditUserPreview() {
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Expanded)
    val viewModelPreview = ItemsViewModelPreview()
    viewModelPreview.currentItemId = remember { mutableStateOf(TextFieldValue("002")) }
    viewModelPreview.currentItemName = remember { mutableStateOf(TextFieldValue("bla")) }
    viewModelPreview.currentItemAmount = remember { mutableStateOf(TextFieldValue(50.toString())) }
    viewModelPreview.currentItemPrice = remember { mutableStateOf(TextFieldValue(5.5.toString())) }
    EditItemView(viewModelPreview, bottomState)
}

@Composable
fun EditItemView(
    viewModel: IItemsViewModel,
    bottomState: ModalBottomSheetState,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EditTitleRow(viewModel.currentItemName.value.text, viewModel.currentItemId.value.text)
        ItemTextField(viewModel.currentItemId, stringResource(R.string.id_hint))
        ItemTextField(viewModel.currentItemName, stringResource(R.string.name_hint))
        ItemTextField(
            viewModel.currentItemAmount,
            stringResource(R.string.amount),
            KeyboardType.Number
        )
        ItemTextField(
            viewModel.currentItemPrice,
            stringResource(R.string.price_hint),
            KeyboardType.Number
        )
        ButtonRow(viewModel, bottomState)
        Box(Modifier.height(50.dp))
    }
}

@Composable
fun EditTitleRow(itemName: String, itemId: String) {
    Row(
        modifier = Modifier.padding(bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "$itemName / $itemId", fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ItemTextField(
    state: MutableState<TextFieldValue>,
    hint: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.value,
            onValueChange = { state.value = it },
            label = { Text(hint) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
fun ButtonRow(viewModel: IItemsViewModel, bottomState: ModalBottomSheetState) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        CustomButton(
            text = "Cancel",
            fraction = 0.3f,
            onClick = { scope.launch { bottomState.hide() } }
        )
        CustomButton(
            text = "Save",
            fraction = 0.4f,
            onClick = {
                scope.launch {
                    viewModel.updateItem()
                    bottomState.hide()
                }
            }
        )
    }
}
