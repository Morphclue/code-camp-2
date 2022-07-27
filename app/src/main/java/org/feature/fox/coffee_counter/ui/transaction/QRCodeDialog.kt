package org.feature.fox.coffee_counter.ui.transaction

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.feature.fox.coffee_counter.R
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
            QRCodeMainDialog(viewModel)
            QRCodeSendDialog(viewModel)
            QRCodeReceiveDialog(viewModel)
        }
    }
}

@Composable
fun QRCodeMainDialog(viewModel: ITransactionViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.qrcode),
            style = MaterialTheme.typography.subtitle1
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f, fill = false)
                .padding(vertical = 16.dp)
        ) {
            CustomButton(
                text = stringResource(R.string.send_money),
            )
            CustomButton(
                text = stringResource(R.string.receive_money),
            )
        }
        QRCodeDialogCancelButton(viewModel)
    }
}

@Composable
fun QRCodeSendDialog(viewModel: ITransactionViewModel) {
    TODO("Not yet implemented")
}

@Composable
fun QRCodeReceiveDialog(viewModel: ITransactionViewModel) {
    TODO("Not yet implemented")
}

@Composable
fun QRCodeDialogCancelButton(viewModel: ITransactionViewModel) {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End) {
        TextButton(onClick = {
            viewModel.qrCodeDialogVisible.value = false
        }) {
            Text(text = stringResource(id = R.string.cancel))
        }
    }
}
