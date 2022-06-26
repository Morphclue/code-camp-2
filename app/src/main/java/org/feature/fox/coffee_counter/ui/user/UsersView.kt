@file:OptIn(ExperimentalMaterialApi::class)

package org.feature.fox.coffee_counter.ui.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.local.database.tables.User
import org.feature.fox.coffee_counter.ui.common.MoneyAppBar
import org.feature.fox.coffee_counter.ui.common.SearchBar

@Preview(showSystemUi = true)
@Composable
fun UsersView() {
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val users = listOf(
        User(id = "a", name = "Julian", isAdmin = true, password = "julian"),
        User(id = "b", name = "Steffen", isAdmin = true, password = "steffen"),
        User(id = "c", name = "Kevin", isAdmin = true, password = "kevin"),
        User(id = "d", name = "Nils", isAdmin = false, password = "nils"),
    )

    ModalBottomSheetLayout(
        sheetState = bottomState,
        sheetContent = {
            EditUserView(users, bottomState)
        }) {
        Scaffold(
            topBar = { MoneyAppBar(title = stringResource(R.string.user_list_title)) },
        ) {
            Column {
                SearchBar()
                UserList(users, bottomState)
            }
        }
    }
}

@Composable
fun UserList(users: List<User>, bottomState: ModalBottomSheetState) {
    Column {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(5.dp)
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            users.forEach { user ->
                UserRow(user, bottomState)
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
fun UserRow(user: User, bottomState: ModalBottomSheetState) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            user.name + " ${if (user.isAdmin) "(${stringResource(id = R.string.user_admin)})" else ""}",
            fontWeight = FontWeight.Medium
        )
        MoneyEditRow(bottomState)
    }
}

@Composable
fun MoneyEditRow(bottomState: ModalBottomSheetState) {
    val scope = rememberCoroutineScope()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
    ) {
        Text(
            //FIXME Extract balance from db
            // - Use observeTotalBalanceOfUser method from UserDao
            "42€",
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            modifier = Modifier.width(60.dp)
        )
        Button(
            onClick = { scope.launch { bottomState.show() } })
        {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit",
            )
        }
    }
}


