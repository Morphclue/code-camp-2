package org.feature.fox.coffee_counter.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.ui.common.CommonTextField

/**
 * Dialog to add a new user.
 * @param viewModel the UserList ViewModel.
 */
@Composable
fun AddUserDialog(
    viewModel: IUserListViewModel,
) {
    if (!viewModel.editDialogVisible.value) {
        return
    }
    Dialog(
        onDismissRequest = { viewModel.editDialogVisible.value = false },
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.create_user),
                    style = MaterialTheme.typography.subtitle1
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(weight = 1f, fill = false)
                        .padding(vertical = 16.dp)
                ) {
                    CommonTextField(
                        state = viewModel.editName,
                        label = stringResource(id = R.string.name_hint),
                    )
                    CommonTextField(
                        state = viewModel.editId,
                        label = stringResource(id = R.string.id_hint),
                    )
                    CommonTextField(
                        state = viewModel.editPassword,
                        label = stringResource(id = R.string.password_hint),
                    )
                    CommonTextField(
                        state = viewModel.editReEnterPassword,
                        label = stringResource(id = R.string.re_enter_password_hint),
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(text = stringResource(R.string.admin_label))
                        Switch(
                            checked = viewModel.isAdminState.value,
                            onCheckedChange = { viewModel.isAdminState.value = it }
                        )
                    }
                }
                AddUserDialogButtons(viewModel)
            }
        }
    }
}

/**
 * Buttons for the AddUserDialog.
 * @param viewModel the UserList ViewModel.
 */
@Composable
fun AddUserDialogButtons(viewModel: IUserListViewModel) {
    val scope = rememberCoroutineScope()
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End) {
        TextButton(onClick = {
            viewModel.editDialogVisible.value = false
        }) {
            Text(text = stringResource(id = R.string.cancel))
        }
        TextButton(onClick = {
            scope.launch {
                viewModel.createUser()
            }
        }) {
            Text(text = stringResource(id = R.string.create_user))
        }
    }
}
