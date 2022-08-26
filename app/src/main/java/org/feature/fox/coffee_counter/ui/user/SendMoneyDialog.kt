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

/**
 * A dialog to send money to a user.
 * @param viewModel the UserList ViewModel.
 */
@Composable
fun SendMoneyDialog(viewModel: IUserListViewModel) {
    if (!viewModel.sendMoneyDialogVisible.value) {
        return
    }

    Dialog(
        onDismissRequest = { viewModel.sendMoneyDialogVisible.value = false },
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface,
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                SendMoneyDialogContent(viewModel)
                SendMoneyDialogButtons(viewModel)
            }
        }
    }
}

/**
 * The content of the send money dialog.
 * @param viewModel the UserList ViewModel.
 */
@Composable
fun SendMoneyDialogContent(viewModel: IUserListViewModel) {
    Text(
        text = stringResource(id = R.string.share_money),
        style = MaterialTheme.typography.subtitle1
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
    ) {
        CommonTextField(
            state = viewModel.sendAmount,
            label = stringResource(id = R.string.amount),
        )
    }
}

/**
 * The buttons of the send money dialog.
 * @param viewModel the UserList ViewModel.
 */
@Composable
fun SendMoneyDialogButtons(viewModel: IUserListViewModel) {
    val coroutineScope = rememberCoroutineScope()

    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End) {
        TextButton(onClick = {
            viewModel.sendMoneyDialogVisible.value = false
        }) {
            Text(text = stringResource(id = R.string.cancel))
        }
        TextButton(onClick = {
            coroutineScope.launch {
                viewModel.sendMoney()
            }
        }) {
            Text(text = stringResource(id = R.string.send))
        }
    }
}


