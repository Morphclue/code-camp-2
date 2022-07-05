@file:OptIn(ExperimentalMaterialApi::class)

package org.feature.fox.coffee_counter.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberModalBottomSheetState
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
import org.feature.fox.coffee_counter.ui.common.LoadingAnimation
import org.feature.fox.coffee_counter.ui.common.MoneyAppBar
import org.feature.fox.coffee_counter.ui.common.SearchBar

@Preview(showSystemUi = true)
@Composable
fun UsersViewPreview() {
    val preview = UserListViewModelPreview()
    preview.userList.add(User(id = "a", name = "Julian", isAdmin = true, password = "julian", 42.0))
    preview.userList.add(User(id = "b",
        name = "Steffen",
        isAdmin = true,
        password = "steffen",
        42.0))
    UsersView(preview)
}

@Composable
fun UsersView(viewModel: IUserListViewModel) {
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val users = listOf(
        User(id = "a", name = "Julian", isAdmin = true, password = "julian", 42.0),
        User(id = "b", name = "Steffen", isAdmin = true, password = "steffen", 42.0),
        User(id = "c", name = "Kevin", isAdmin = true, password = "kevin", 42.0),
        User(id = "d", name = "Nils", isAdmin = false, password = "nils", 42.0),
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
                if (viewModel.isLoaded.value) UserList(viewModel, bottomState) else LoadingBox()
            }
        }
    }
}

@Composable
fun LoadingBox() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoadingAnimation()
    }
}

@Composable
fun UserList(viewModel: IUserListViewModel, bottomState: ModalBottomSheetState) {
    Column {
        Column(
            modifier = Modifier
                .verticalScroll(viewModel.scrollState)
                .padding(5.dp)
                .fillMaxWidth()
                .weight(1f)
                .height((IntrinsicSize.Min)),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            viewModel.userList.forEach { user ->
                UserRow(user, bottomState)
                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 1.dp
                )
            }
            Box(Modifier.height(30.dp))
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



