package org.feature.fox.coffee_counter.ui.transaction

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.ui.common.CommonTextField
import org.feature.fox.coffee_counter.ui.common.CustomButton

@Preview
@Composable
fun QRCodeDialogPreview() {
    QRCodeDialog(TransactionViewModelPreview())
}

@Composable
fun QRCodeDialog(viewModel: ITransactionViewModel) {
    if (!viewModel.qrCodeDialogVisible.value) {
        return
    }

    Dialog(
        onDismissRequest = { viewModel.qrCodeDialogVisible.value = false },
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface,
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                if (viewModel.qrCodeReceiveState.value) {
                    QRCodeReceiveDialog(viewModel)
                }

                if (viewModel.qrCodeSendState.value) {
                    QRCodeSendDialog(viewModel)
                }

                if (!viewModel.qrCodeSendState.value && !viewModel.qrCodeReceiveState.value) {
                    QRCodeMainDialog(viewModel)
                }
            }
        }
    }
}

@Composable
fun QRCodeMainDialog(viewModel: ITransactionViewModel) {
    Text(
        text = stringResource(id = R.string.qrcode),
        style = MaterialTheme.typography.subtitle1
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        CustomButton(
            text = stringResource(R.string.send_money),
            onClick = { viewModel.qrCodeSendState.value = true }
        )
        CustomButton(
            text = stringResource(R.string.receive_money),
            onClick = { viewModel.qrCodeReceiveState.value = true }
        )
    }
    QRCodeDialogButtons(viewModel)
}

@Composable
fun QRCodeSendDialog(viewModel: ITransactionViewModel) {
    Text(
        text = stringResource(id = R.string.send_money),
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
    QRCodeDialogButtons(viewModel)
}

@Composable
fun QRCodeReceiveDialog(viewModel: ITransactionViewModel) {
    Text(
        text = stringResource(id = R.string.receive_money),
        style = MaterialTheme.typography.subtitle1
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val painter = rememberAsyncImagePainter(viewModel.qrCode.value)

        Image(
            painter = painter,
            contentDescription = stringResource(R.string.qrcode),
            modifier = Modifier
                .wrapContentSize(),
        )
    }
    QRCodeDialogButtons(viewModel)
}

@Composable
fun QRCodeDialogButtons(viewModel: ITransactionViewModel) {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End) {
        TextButton(onClick = {
            viewModel.qrCodeDialogVisible.value = false
            viewModel.qrCodeSendState.value = false
            viewModel.qrCodeReceiveState.value = false
        }) {
            Text(text = stringResource(id = R.string.cancel))
        }
        if (viewModel.qrCodeSendState.value) {
            TextButton(onClick = {
                // TODO: implement send logic
            }) {
                Text(text = stringResource(id = R.string.send))
            }
        }
    }
}
