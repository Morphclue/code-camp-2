package org.feature.fox.coffee_counter.ui.transaction

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.ui.common.CustomButton
import org.feature.fox.coffee_counter.ui.common.MoneyAppBar
import java.text.SimpleDateFormat
import java.util.*

private val rowTextFontSize: TextUnit = 18.sp


@Preview(showSystemUi = true)
@Composable
fun HistoryViewPreview(
) {
    HistoryView(viewModel = TransactionViewModelPreview())
}

@Composable
fun HistoryView(
    viewModel: ITransactionViewModel,
) {
    QRCodeDialog(viewModel)
    Column {
        MoneyAppBar(Pair(stringResource(R.string.history_title), viewModel.balance))
        ShowPeriodField()
        TransactionContainer(viewModel)
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun TransactionContainer(viewModel: ITransactionViewModel) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(5.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        coroutineScope.launch {
            viewModel.refreshTransactions()
            viewModel.getTotalBalance()
        }
        QRCodeButton(viewModel)
        if (viewModel.transactions.isEmpty()) Text(
            stringResource(id = R.string.no_data),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.LightGray
        )

        viewModel.transactions.forEach { transaction ->
            if (transaction.type == "funding") {
                TransactionRow(
                    Color.Green,
                    "Funding",
                    SimpleDateFormat(BuildConfig.DATE_PATTERN, Locale.GERMAN)
                        .format(Date(transaction.timestamp)),
                    "${String.format("%.2f", transaction.value)}€"
                )
            } else if (transaction.type == "purchase") {
                TransactionRow(
                    Color.DarkGray,
                    "Order",
                    SimpleDateFormat(BuildConfig.DATE_PATTERN, Locale.GERMAN)
                        .format(Date(transaction.timestamp)),
                    "${String.format("%.2f", transaction.value)}€"
                )
            }
        }
    }
}

@Composable
fun QRCodeButton(viewModel: ITransactionViewModel) {
    val coroutineScope = rememberCoroutineScope()
    CustomButton(
        text = stringResource(R.string.qrcode),
        fraction = 0.9f,
        onClick = {
            coroutineScope.launch {
                viewModel.shareQRCode()
            }
        }
    )

    if (viewModel.qrCode.value == null) {
        return
    }
    val painter = rememberAsyncImagePainter(viewModel.qrCode.value)

    Image(
        painter = painter,
        contentDescription = stringResource(R.string.qrcode),
        modifier = Modifier
            .wrapContentSize()
            .size(150.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ShowPeriodField() {
    // TODO: implement searchbar
}

@Composable
fun TransactionRow(color: Color, type: String, date: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = 5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TransactionCircle(color)
            TransactionType(type)
            TransactionDate(date)
            TransactionValue(color, value)
        }
    }
}

@Composable
fun TransactionType(type: String) {
    Text(
        modifier = Modifier.width(80.dp),
        text = type,
        fontSize = rowTextFontSize,
        textAlign = TextAlign.Start
    )
}

@Composable
fun TransactionCircle(color: Color) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .clip(shape = CircleShape)
            .background(color)
    )
}

@Composable
fun TransactionDate(date: String) {
    Text(
        modifier = Modifier.width(100.dp),
        text = date,
        fontSize = rowTextFontSize,
        color = Color.Gray
    )
}

@Composable
fun TransactionValue(color: Color, value: String) {
    Text(
        modifier = Modifier.width(100.dp),
        text = value,
        fontSize = rowTextFontSize,
        color = color,
        textAlign = TextAlign.End
    )
}
