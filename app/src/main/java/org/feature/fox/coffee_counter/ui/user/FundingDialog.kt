package org.feature.fox.coffee_counter.ui.user

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

/**
 * Dialog to create send funding.
 * @param viewModel the UserList ViewModel.
 */
@Composable
fun FundingDialog(
    viewModel: IUserListViewModel,
) {
    if (!viewModel.fundingDialogVisible.value) {
        return
    }
    Dialog(
        onDismissRequest = { viewModel.fundingDialogVisible.value = false },
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

/**
 * Buttons for the funding dialog.
 * @param viewModel the UserList ViewModel.
 */
@Composable
fun FundingDialogButtons(viewModel: IUserListViewModel) {
    val scope = rememberCoroutineScope()
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End) {
        TextButton(onClick = {
            viewModel.fundingDialogVisible.value = false
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
