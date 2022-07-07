package org.feature.fox.coffee_counter.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.ui.common.CommonTextField

@Composable
fun EditUserDialog(
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
                    text = "Update ${viewModel.currentUser.value?.name}",
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
                }
                EditUserDialogButtons(viewModel)
            }
        }
    }
}

@Composable
fun EditUserDialogButtons(viewModel: IUserListViewModel) {
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
                viewModel.updateUser()
            }
        }) {
            Text(text = stringResource(id = R.string.update_profile))
        }
    }
}
