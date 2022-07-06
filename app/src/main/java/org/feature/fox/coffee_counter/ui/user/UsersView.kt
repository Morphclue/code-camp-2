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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.models.response.UserIdResponse
import org.feature.fox.coffee_counter.ui.common.CommonTextField
import org.feature.fox.coffee_counter.ui.common.LoadingAnimation
import org.feature.fox.coffee_counter.ui.common.MoneyAppBar
import org.feature.fox.coffee_counter.ui.common.SearchBar
import org.feature.fox.coffee_counter.ui.common.ToastMessage

@Preview(showSystemUi = true)
@Composable
fun UsersViewPreview() {
    val preview = UserListViewModelPreview()
    preview.userList.add(UserIdResponse("a", "Julian", 15.0))
    preview.userList.add(UserIdResponse("b", "Kevin", -15.0))
    preview.userList.add(UserIdResponse("c", "Steffen", 42.0))
    UsersView(preview)
}

@Composable
fun UsersView(viewModel: IUserListViewModel) {
    val context = LocalContext.current
    ToastMessage(viewModel, context)
    FundingDialog(viewModel)

    Scaffold(
        topBar = { MoneyAppBar(title = stringResource(R.string.user_list_title)) },
    ) {
        Column {
            SearchBar()
            if (viewModel.isLoaded.value) UserList(viewModel) else LoadingBox()
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
fun UserList(viewModel: IUserListViewModel) {
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
                UserRow(viewModel, user)
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
fun UserRow(
    viewModel: IUserListViewModel,
    user: UserIdResponse,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            user.name,
            fontWeight = FontWeight.Medium
        )
        MoneyEditRow(viewModel, user)
    }
}

@Composable
fun MoneyEditRow(
    viewModel: IUserListViewModel,
    user: UserIdResponse,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
    ) {
        Text(
            "${user.balance}€",
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            modifier = Modifier.width(60.dp)
        )
        Button(
            onClick = {
                viewModel.currentUser.value = user
                viewModel.dialogVisible.value = true
            })
        {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add",
            )
        }
    }
}

@Composable
fun FundingDialog(
    viewModel: IUserListViewModel,
) {
    if (!viewModel.dialogVisible.value) {
        return
    }
    Dialog(
        onDismissRequest = { viewModel.dialogVisible.value = false },
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Add funding for ${viewModel.currentUser.value?.name}",
                    style = MaterialTheme.typography.subtitle1
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(weight = 1f, fill = false)
                        .padding(vertical = 16.dp)
                ) {
                    CommonTextField(
                        state = viewModel.funding,
                        label = stringResource(id = R.string.amount),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number)
                    )
                }
                FundingDialogButtons(viewModel)
            }
        }
    }
}

@Composable
fun FundingDialogButtons(viewModel: IUserListViewModel) {
    val scope = rememberCoroutineScope()
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End) {
        TextButton(onClick = {
            viewModel.dialogVisible.value = false
        }) {
            Text(text = stringResource(id = R.string.cancel))
        }
        TextButton(onClick = {
            scope.launch {
                viewModel.addFunding()
            }
        }) {
            Text(text = stringResource(id = R.string.ok))
        }
    }
}



