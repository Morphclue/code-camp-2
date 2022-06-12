package org.feature.fox.coffee_counter.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.local.database.tables.User

class UserListProvider : PreviewParameterProvider<List<User>> {
    override val values: Sequence<List<User>> = sequenceOf(
        listOf(
            User(id = "a", name = "Nils", isAdmin = false, password = "nils"),
            User(id = "b", name = "Max", isAdmin = true, password = "max"),
        )
    )
}

@Preview
@Composable
fun EditUserView(@PreviewParameter(UserListProvider::class) users: List<User>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserNameRow(users[0].name)
        MoneyRow()
        AdminRow(users[0].isAdmin)
        ButtonRow()
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
fun ButtonRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        CustomButton(
            text = "Cancel",
            fraction = 0.3f,
            onClick = {}
        )
        CustomButton(
            text = "Save",
            fraction = 0.4f,
            onClick = {}
        )
    }
}

