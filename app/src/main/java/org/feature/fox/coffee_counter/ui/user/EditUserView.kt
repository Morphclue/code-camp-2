@file:OptIn(ExperimentalMaterialApi::class)

package org.feature.fox.coffee_counter.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.local.database.tables.User
import org.feature.fox.coffee_counter.ui.common.CustomButton

@Preview(showBackground = true)
@Composable
fun EditUserPreview() {
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Expanded)
    val viewModelPreview = UserListViewModelPreview()
    viewModelPreview.currentUser.value = User(
        "Preview",
        "Peter",
        true,
        "1234",
        0.0
    )
    EditUserView(viewModelPreview, bottomState)
}

@Composable
fun EditUserView(
    viewModel: IUserListViewModel,
    bottomState: ModalBottomSheetState,
) {
    val user = viewModel.currentUser.observeAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        user.value?.name?.let { UserNameRow(it) }
        MoneyRow()
        user.value?.isAdmin?.let { AdminRow(it) }
        ButtonRow(viewModel, bottomState)
        Box(Modifier.height(50.dp))
    }
}

@Composable
fun UserNameRow(userName: String) {
    Row(
        modifier = Modifier.padding(bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = userName, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun MoneyRow() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val moneyState = remember { mutableStateOf(TextFieldValue()) }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = moneyState.value,
            onValueChange = { moneyState.value = it },
            label = {
                Text(
                    text = stringResource(R.string.money_hint)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
fun AdminRow(admin: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.admin_label),
            fontWeight = FontWeight.Medium
        )
        val checkedState = remember { mutableStateOf(admin) }
        Checkbox(
            checked = checkedState.value,
            onCheckedChange = { checkedState.value = it }
        )
    }
}

@Composable
fun ButtonRow(viewModel: IUserListViewModel, bottomState: ModalBottomSheetState) {
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
                    viewModel.updateUser()
                    bottomState.hide()
                }
            }
        )
    }
}

