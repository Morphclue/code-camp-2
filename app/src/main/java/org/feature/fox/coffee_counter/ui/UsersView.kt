package org.feature.fox.coffee_counter.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.local.database.tables.User

@Preview(showSystemUi = true)
@Composable
fun UsersView() {
    val users = listOf(
        User(id = "a", name = "Julian", isAdmin = true, password = "julian"),
        User(id = "b", name = "Steffen", isAdmin = true, password = "steffen"),
        User(id = "c", name = "Kevin", isAdmin = true, password = "kevin"),
        User(id = "d", name = "Nils", isAdmin = false, password = "nils"),
    )
    Column {
        MoneyAppBar(title = stringResource(R.string.user_list_title))
        SearchBar()
        UserList(users)
    }
}

@Composable
fun UserList(users: List<User>) {
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
                UserRow(user)
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
fun UserRow(user: User) {
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
        Text(
            //FIXME Extract balance from db
            // - Use observeTotalBalanceOfUser method from UserDao
            "42€",
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            modifier = Modifier.width(60.dp)
        )
    }
}



